package study.preview;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @Author shubiao
 * @Created 2023/04/28 15:39
 */
public class PdfToHtmlConverter {

    public static void main(String[] args) {
            try {
                // 加载 PDF 文件
                PDDocument document = PDDocument.load(new File("C:\\Users\\shubiao\\Downloads\\简历文件预览.pdf"));

                // 创建 PDF 渲染器
                PDFRenderer renderer = new PDFRenderer(document);

                // 循环处理每一页
                for (int i = 0; i < document.getNumberOfPages(); i++) {
                    // 渲染 PDF 页面为 BufferedImage
                    BufferedImage image = renderer.renderImageWithDPI(i, 300);

                    // 将 BufferedImage 转换为 SVG
                    ByteArrayOutputStream svgOutputStream = new ByteArrayOutputStream();
                    String[] command = new String[]{"convert", "-", "svg:-"};
                    Process process = Runtime.getRuntime().exec(command);
                    ImageIO.write(image, "png", process.getOutputStream());
                    IOUtils.copy(process.getInputStream(), new BufferedOutputStream(svgOutputStream));
                    IOUtils.closeQuietly(process.getOutputStream());
                    IOUtils.closeQuietly(process.getInputStream());
                    IOUtils.closeQuietly(process.getErrorStream());

                    // 将 SVG 写入 HTML 文件
                    String svgString = new String(svgOutputStream.toByteArray(), StandardCharsets.UTF_8);
                    String htmlString = String.format("<html><body>%s</body></html>", svgString);
                    FileUtils.writeStringToFile(new File(String.format("page%d.html", i + 1)), htmlString, StandardCharsets.UTF_8);
                }

                // 关闭 PDF 文件
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
