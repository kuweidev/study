package study.preview;

/**
 * @Author shubiao
 * @Created 2023/04/28 13:41
 */

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfToImageConverter {


    /**
     * PDF转换图片
     *
     * @param pdfPath 示例：C:\Users\shubiao\Downloads\简历文件预览.pdf
     */
    public static List<String> listPdfToImage(String pdfPath) {
        try {
            // 加载 PDF 文件
            PDDocument document = PDDocument.load(new File(pdfPath));

            // 指定保存图片的目录
            String savePath = "E:\\Study\\study\\preview\\src\\main\\resources\\images\\";

            // 创建 PDF 渲染器
            PDFRenderer renderer = new PDFRenderer(document);

            // 循环处理每一页
            List<String> imagePathList = new ArrayList<>();
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                // 渲染 PDF 页面为 BufferedImage，如果生成的图片质量不理想，可以调整 dpi 参数，增加分辨率来提高清晰度。
                int dpi = 300;
                BufferedImage image = renderer.renderImageWithDPI(i, dpi, ImageType.RGB);

                // 保存 BufferedImage 为 PNG 文件
                String imagePath = savePath + "page" + (i + 1) + ".png";
                ImageIO.write(image, "png", new File(imagePath));
                imagePathList.add(imagePath);
            }

            // 关闭 PDF 文件
            document.close();
            return imagePathList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void generateHtml(List<String> imageUrls) {
        try {
            File htmlFile = new File("E:\\Study\\study\\preview\\src\\main\\resources\\html", "preview" + ".html");
            FileWriter writer = new FileWriter(htmlFile);
            writer.write("<html>\n" +
                    "<head>\n" +
                    "\t<title>文件预览</title>\n" +
                    "\t<style>\n" +
                    "\t\tbody {\n" +
                    "\t\t\tmargin: 0;\n" +
                    "\t\t\tpadding: 0;\n" +
                    "\t\t\tdisplay: flex;\n" +
                    "\t\t\tflex-direction: column;\n" +
                    "\t\t\talign-items: center;\n" +
                    "\t\t\tjustify-content: center;\n" +
                    "\t\t\tmin-height: 100vh;\n" +
                    "\t\t\tbackground-color: #f5f5f5;\n" +
                    "\t\t}\n" +
                    "\t\timg {\n" +
                    "\t\t\tmax-width: 100%;\n" +
                    "\t\t\theight: auto;\n" +
                    "\t\t\tmargin: 20px 0;\n" +
                    "\t\t}\n" +
                    "\t</style>\n" +
                    "</head>\n" +
                    "<body>");
            for (String imageUrl : imageUrls) {
                writer.write("<img src=\"" + imageUrl + "\"/>\n");
            }
            writer.write("</body>\n</html>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<String> imagePathList = listPdfToImage("C:\\Users\\shubiao\\Downloads\\简历文件预览.pdf");
        if (null != imagePathList && !imagePathList.isEmpty()){
            generateHtml(imagePathList);
            System.out.println("生成完成");
        }
    }


}
