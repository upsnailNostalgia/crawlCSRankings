import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: crawlCSRankings
 * @Package: PACKAGE_NAME
 * @ClassName: CrawlMain2
 * @Description:
 * @Author: bruce
 * @CreateDate: 2019/11/16 22:39
 * @Version: 1.0
 */
public class CrawlMain2 {
//    public static void main(String[] args) throws IOException {
////        CloseableHttpClient httpclient = HttpClients.createDefault();
////        HttpGet httpGet = new HttpGet("http://csrankings.org/#/index?soft");
////        CloseableHttpResponse response = httpclient.execute(httpGet);
////        if (response.getStatusLine().getStatusCode() == 200) {
////            HttpEntity entity = response.getEntity();
////            String body = EntityUtils.toString(entity, "GBK");
////            System.out.println(body);
////        }
//
//        System.getProperties().setProperty("webdriver.chrome.driver","G:/crawlCSRankings/chromedriver78.exe");
//        //开启webDriver进程
//        WebDriver webDriver = new ChromeDriver();
//        webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
//        while (true) {
//            try {
//                webDriver.get("http://jianshu.com");
//                break;
//            } catch (Exception e) {
//                System.out.println("错误！！！");
//            }
//        }
//
//        List<WebElement> webElements = webDriver.findElements(By.xpath("//*[@class='col-xs-7 col-xs-offset-1 aside']/div/a"));
//        System.out.println(webElements.size());
//        for (WebElement ele :
//                webElements) {
//            System.out.println(ele.toString());
//        }
//    }
public static void main(String[] args) throws InterruptedException, IOException {
    System.getProperties().setProperty("webdriver.chrome.driver","G:/crawlCSRankings/chromedriver78.exe");
    //开启webDriver进程
    WebDriver webDriver = new ChromeDriver();
    webDriver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
    while (true) {
        try {
            webDriver.get("http://csrankings.org/#/index?none");
            break;
        } catch (Exception e) {
            System.out.println("错误！！！");
//                Thread.sleep(10000);
        }
    }
    System.out.println("==========get()已经执行完毕！==========");
    Thread.sleep(10000);
    Select select = new Select(webDriver.findElement(By.id("regions")));
    //国家地区：中国，亚洲（除中国），美国，加拿大，欧洲，大洋洲，全球
    List<String> regions = Arrays.asList("USA");
    for (String region : regions) {
        String outputPath = "G:\\crawlCSRankings\\output2\\"+region+".txt";
        System.out.println("=======region为："+region+"=========");
        select.selectByValue(region);
        //各领域：软件工程(Software engineering)：ICSE,FSE,ASE,ISSTA
        //      系统软件(Operating systems)：OSDI,SOSP
        //      程序设计语言(Programming languages)：PLDI,POPL,OOPSLA
        //      软件基础理论(Algorithms & complexity)：FOCS,SODA,STOC,
        //                  (Logic & verification):CAV,LICS
        //      数据管理(Databases)：SIGMOD,VLDB,ICDE,
        //              (Machine learning & data mining):KDD,
        //              (The Web & information retrieva):SIGIR


        HashMap<String,List<String>> domainMeeting = new HashMap<String, List<String>>();
        domainMeeting.put("ops-widget",Arrays.asList("osdi","sosp"));

        for (Map.Entry<String,List<String>> entry:domainMeeting.entrySet()) {
            String domain = entry.getKey();
            System.out.println("=======domain为："+domain+"======");
            webDriver.findElement(By.id(domain)).click();

            for (String meeting : entry.getValue()) {
                webDriver.findElement(By.id(meeting)).click();
                Thread.sleep(20000);
                System.out.println("=======meeting为："+meeting+"=======");

                System.out.println("++++++++++++++++++++++++++++++++");
//                    writeHTML("G:\\crawlCSRankings\\output\\tmp.html",webDriver.getPageSource());
                System.out.println(webDriver.getPageSource());
                System.out.println("++++++++++++++++++++++++++++++++");

                //处理缓存到本地的html文件，目的是获取所有ranking的学校信息的左边按钮的id
                //System.out.println(webDriver.findElement(By.xpath("//table[@id='ranking']/tbody")).findElements(By.tagName("tr")).size());
                List<WebElement> trList = webDriver.findElements(By.xpath("//table[@id='ranking']/tbody/tr"));
                webDriver.findElements(By.xpath("//table[@id='ranking']/tbody/tr")).get(117).findElements(By.tagName("td")).get(1).findElement(By.className("hovertip")).click();
                Thread.sleep(10000);
                System.out.println("trList的大小为："+trList.size());
                for (WebElement ele : trList) {System.out.println(ele.getText());}
                //获取右边ranking的table里面的数据
                for (int i = 0; i < trList.size();) {
                    //界面显示是每三个tr是一个规律，先点击第一个tr的按钮，再获取第三个tr中的有用数据
                    if (i%3==0){
                        List<WebElement> tdList = trList.get(i).findElements(By.tagName("td"));
                        tdList.get(1).findElement(By.className("hovertip")).click();
                        i+=2;
                    }
                    else if (i%3==2){
                        WebElement theadbody = trList.get(i).findElement(By.xpath(".//td/div/div/table"));
                        List<WebElement> tbodytrs = theadbody.findElements(By.xpath(".//tbody/tr"));
                        System.out.println("tbodytrs大小为:"+tbodytrs.size());
                        for (int j = 0; j < tbodytrs.size(); j=j+2) {
                            List<WebElement> tbodytrstds = tbodytrs.get(j).findElements(By.tagName("td"));
                            //可能存在越级find tagname
                            String teacher = tbodytrstds.get(1).findElements(By.xpath(".//small/a")).get(0).getText();
                            System.out.println(teacher+"==========");
                            String adjVal = tbodytrstds.get(3).findElement(By.tagName("small")).getText();
                            System.out.println(adjVal+"==========");
                        }
                        i++;
                    }

                }
                webDriver.findElement(By.id(meeting)).click();//将这个回合点击的会议按钮再点击一次，表示取消
            }
            webDriver.findElement(By.id(domain)).click();//将这个domain的按钮点击收回去
        }
    }

    webDriver.close();
    webDriver.quit();
}
}
