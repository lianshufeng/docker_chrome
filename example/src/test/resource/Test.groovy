import org.openqa.selenium.WebDriver

WebDriver webDriver = getProperty('driver');

webDriver.get('http://www.baidu.com')
println webDriver.getTitle()