package tektonikal.crystalchams.util;
// Source - https://stackoverflow.com/a
// Posted by litetex
// Retrieved 2025-12-02, License - CC BY-SA 4.0

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AnimatedGIFDecoder
{
    public List<BufferedImage> resolve(final byte[] imageData)
            throws IOException
    {
        final ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
        try(final ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(imageData)))
        {
            reader.setInput(in);
            int numImages = reader.getNumImages(true);

            final MetadataInfo metadataInfo = this.resolveMetaDataInfo(reader.getStreamMetadata());

            return this.resolveFrames(reader, metadataInfo, numImages);
        }
        finally
        {
            reader.dispose();
        }
    }

    protected MetadataInfo resolveMetaDataInfo(final IIOMetadata metadata)
    {
        if(metadata == null)
        {
            return new MetadataInfo(-1, -1, null);
        }

        int width = -1;
        int height = -1;
        Color backgroundColor = null;

        final IIOMetadataNode globalRoot =
                (IIOMetadataNode)metadata.getAsTree(metadata.getNativeMetadataFormatName());

        final NodeList globalScreeDescriptor = globalRoot.getElementsByTagName("LogicalScreenDescriptor");
        if(globalScreeDescriptor.getLength() > 0)
        {
            final IIOMetadataNode screenDescriptor = (IIOMetadataNode)globalScreeDescriptor.item(0);

            if(screenDescriptor != null)
            {
                width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
                height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
            }
        }

        final NodeList globalColorTable = globalRoot.getElementsByTagName("GlobalColorTable");
        if(globalColorTable.getLength() > 0)
        {
            final IIOMetadataNode colorTable = (IIOMetadataNode)globalColorTable.item(0);
            if(colorTable != null)
            {
                final String bgIndex = colorTable.getAttribute("backgroundColorIndex");

                IIOMetadataNode colorEntry = (IIOMetadataNode)colorTable.getFirstChild();
                for(int safeGuard = 0; colorEntry != null && safeGuard < 1_000_000; safeGuard++)
                {
                    if(colorEntry.getAttribute("index").equals(bgIndex))
                    {
                        final int red = Integer.parseInt(colorEntry.getAttribute("red"));
                        final int green = Integer.parseInt(colorEntry.getAttribute("green"));
                        final int blue = Integer.parseInt(colorEntry.getAttribute("blue"));

                        backgroundColor = new Color(red, green, blue);
                        break;
                    }

                    colorEntry = (IIOMetadataNode)colorEntry.getNextSibling();
                }
            }
        }
        return new MetadataInfo(width, height, backgroundColor);
    }

    protected List<BufferedImage> resolveFrames(
            final ImageReader reader,
            final MetadataInfo metadataInfo,
            final int numImages) throws IOException
    {
        final int logicalScreenWidth = metadataInfo.logicalScreenWidth();
        final int logicalScreenHeight = metadataInfo.logicalScreenHeight();

        BufferedImage baseImage = null;

        final List<BufferedImage> frames = new ArrayList<>(numImages);
        for(int frameIndex = 0; frameIndex < numImages; frameIndex++)
        {
            final BufferedImage image = reader.read(frameIndex);

            final int width = image.getWidth();
            final int height = image.getHeight();

            final int maxWidth = Math.min(width, logicalScreenWidth);
            final int maxHeight = Math.min(height, logicalScreenHeight);
            if(baseImage == null)
            {
                baseImage = new BufferedImage(logicalScreenWidth, logicalScreenHeight, BufferedImage.TYPE_INT_ARGB);
            }

            final FrameMetaData frameMetaData = this.resolveFrameMetaData(reader, frameIndex);
            final int imageX = frameMetaData.x();
            final int imageY = frameMetaData.y();

            final Rectangle area = new Rectangle(imageX, imageY, maxWidth, maxHeight);

            // Create a backup bufferedImage from the base image for the area of the current frame
            final BufferedImage backup = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
            backup.setData(baseImage.getData(area));

            // Draw this frame to the base
            final Graphics2D g = baseImage.createGraphics();
            g.drawImage(image, imageX, imageY, null);

            // We need to clone the base image since we are going to dispose it later according to the disposal method
            final BufferedImage clone =
                    new BufferedImage(logicalScreenWidth, logicalScreenHeight, BufferedImage.TYPE_INT_ARGB);
            clone.setData(baseImage.getData());

            if("restoreToBackgroundColor".equals(frameMetaData.disposal()))
            {
                final Composite oldComposite = g.getComposite();
                g.setComposite(AlphaComposite.Clear);
                g.fillRect(imageX, imageY, width, height);
                g.setComposite(oldComposite);
            }
            else if("restoreToPrevious".equals(frameMetaData.disposal()))
            {
                final Composite oldComposite = g.getComposite();
                g.setComposite(AlphaComposite.Src);
                g.drawImage(backup, imageX, imageY, null);
                g.setComposite(oldComposite);
            }
            g.dispose();

            frames.add(clone);
        }
        return frames;
    }

    protected FrameMetaData resolveFrameMetaData(final ImageReader reader, final int frameIndex) throws IOException
    {
        final IIOMetadataNode root =
                (IIOMetadataNode)reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
        final IIOMetadataNode gce = (IIOMetadataNode)root.getElementsByTagName("GraphicControlExtension").item(0);
        final NodeList children = root.getChildNodes();
        final String disposal = gce.getAttribute("disposalMethod");
        int x = 0;
        int y = 0;
        for(int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++)
        {
            final Node nodeItem = children.item(nodeIndex);

            if(nodeItem.getNodeName().equals("ImageDescriptor"))
            {
                final NamedNodeMap map = nodeItem.getAttributes();

                x = Integer.parseInt(map.getNamedItem("imageLeftPosition").getNodeValue());
                y = Integer.parseInt(map.getNamedItem("imageTopPosition").getNodeValue());
            }
        }
        return new FrameMetaData(x, y, disposal);
    }

    protected record FrameMetaData(int x, int y, String disposal)
    {
    }


    protected record MetadataInfo(int logicalScreenWidth, int logicalScreenHeight, Color backgroundColor)
    {
    }
}
