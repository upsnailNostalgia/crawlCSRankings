import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * @ProjectName: crawlCSRankings
 * @Package: PACKAGE_NAME
 * @ClassName: StopLoadPage
 * @Description:
 * @Author: bruce
 * @CreateDate: 2019/11/16 21:44
 * @Version: 1.0
 */
public class StopLoadPage extends Thread{
    WebDriver driver = null;
    int sec = 0;
    public StopLoadPage(WebDriver driver, int sec){
        this.driver = driver;
        this.sec = sec;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ((JavascriptExecutor) driver).executeScript("window.stop();");
    }
}
