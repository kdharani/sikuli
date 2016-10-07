package selenium_sikuli.sikuli;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;


public class Example extends Region {
	private static WebDriver driver;
	private static Screen screen = new Screen();
	private static String basedir = System.getProperty("user.dir");
	
	public Example() {

	}
	
	public static void startDriver(){
		System.setProperty("webdriver.chrome.driver", basedir+"\\driver\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	public static void delay(int seconds){
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void stopDriver() throws InterruptedException{
		delay(10000);
		driver.quit();
	}
	
	public static void signIn() throws FindFailed{
		Pattern username_img = new Pattern(basedir+"\\resources\\username.png");
		Pattern password_img = new Pattern(basedir+"\\resources\\password.png");
		Pattern tenantid_img = new Pattern(basedir+"\\resources\\tenantId.png");
		Pattern signin_img = new Pattern(basedir+"\\resources\\signIn.png");
		
		driver.get("https://delb.bifreedom.com");
		screen.type(username_img, "admin");
		screen.type(password_img, "sealindia");
		screen.type(tenantid_img, "WAT110173");
		screen.click(signin_img);
		delay(15000);
	}
	
	public static void launchBuilder() throws FindFailed{
		Pattern builder_img = new Pattern(basedir+"\\resources\\integrationBuilder_link.png");
		screen.wait(builder_img);
		screen.click(builder_img);
		delay(20000);
		switchToTab();
	}
	
	public static void openMyToolboxTab() throws FindFailed{
		Pattern mytoolbox_img = new Pattern(basedir+"\\resources\\myToolbox_tab.png");
		//screen.wait(mytoolbox_img);
		//screen.click(mytoolbox_img);
		Match mytoolbox = screen.exists(mytoolbox_img.similar((float)0.55));
		if(mytoolbox!=null){
			System.out.println("Mytoolbox exists");
		}else{
			System.out.println("Mytoolbox does not exists");
		}
		screen.click(mytoolbox);
		//driver.findElement(By.xpath("//div[@id='tools']//a[@href='#toolbox']")).click();
		screen.wait((double)0.5);
	}
	
	public static void addSqlAction() throws Exception{
		Pattern freedomDb_img = new Pattern(basedir+"\\resources\\freedomDb_link.png");
		Pattern sqlSelect_img = new Pattern(basedir+"\\resources\\sqlSelect_action.png");
		Pattern canvas_img = new Pattern(basedir+"\\resources\\canvas.png");
		//driver.findElement(By.xpath("//div[@id='toolbox']//label[contains(text(),'Freedom DB')]")).click();
		Match freedomDb = screen.exists(freedomDb_img.similar((float)0.70));		
		screen.click(freedomDb);
		
		Match sqlSelect = screen.exists(sqlSelect_img.similar((float)0.70));
		Match canvas = screen.exists(canvas_img.similar((float)0.70));
		if(sqlSelect!=null){
			System.out.println("SQL Select exists");
		}else{
			System.out.println("SQL Select  does not exists");
		}
		if(canvas!=null){
			System.out.println("canvas exists");
		}else{
			System.out.println("canvas  does not exists");
		}
		
		screen.dragDrop(sqlSelect, canvas);
		
		delay(5000);
	}
	
	public static void configureSQLSelectProp() throws Exception{
		WebElement popupModal = driver.findElement(By.xpath("//div[@id='popupModal']"));
		while(!(popupModal.isDisplayed())){
			addSqlAction();
		}
		driver.findElement(By.xpath("//div[@id='popupModal']//label[contains(text(),'Tables/Views:')]/preceding-sibling::input[@name='choice']")).click();
		Select selectTable = new Select(driver.findElement(By.id("searchList")));
		selectTable.selectByValue("ATBLB");
		driver.findElement(By.xpath("//div[@id='popupModal']//a[contains(text(),'OK')]")).click();;
		delay(3000);
	}
	
	public static void bosMapping() throws Exception{
		WebElement bosRoot = driver.findElement(By.xpath("//ul[@id='BOSroot']//label[contains(text(),'BOS')]/preceding-sibling::span"));
		WebElement bosOpTable = driver.findElement(By.xpath("//ul[@id='BOSroot']//ul[@id='BOS3']"));
		WebElement sqlSelectRoot = driver.findElement(By.xpath("//ul[@id='sql_selectroot']//label[contains(text(),'sql_select')]/preceding-sibling::span"));
		WebElement sqlOutput = driver.findElement(By.xpath("//ul[@id='sql_select6']//label[contains(text(),'Output')]/preceding-sibling::span"));
		WebElement outputTable = driver.findElement(By.xpath("//ul[@id='sql_select7']"));
		WebElement bosOpTableArrow = driver.findElement(By.xpath("//ul[@id='BOSroot']//label[contains(text(),'Output')]/preceding-sibling::span"));
		WebElement addBtn = driver.findElement(By.xpath("//div[@id='expression']//input[@name='Add']"));
		
		sqlSelectRoot.click();
		sqlOutput.click();
		bosRoot.click();
		bosOpTableArrow.click();
		/*Actions builder = new Actions(driver);
		Action drag = builder.clickAndHold(outputTable).build();
		drag.perform();
		
		Point coordinates = bosOpTable.getLocation();
		Robot robot = new Robot(); //Robot for controlling mouse actions
		robot.mouseMove(coordinates.getX(),coordinates.getY()+100);*/
		dragAndDropElement(outputTable, bosOpTable,0);
		delay(2000);
		selectCheckboxes();
		WebElement bosOutput = driver.findElement(By.xpath("//ul[@id='BOSroot']//ul[@id='BOS4']"));
		dragAndDropElement(outputTable, bosOutput,0);
		addBtn.click();
		selectOptions();
		
	}
	
	public static void selectCheckboxes(){
		List<WebElement> checkboxes = driver.findElements(By.xpath("//div[@id='popupModal']//input[@name='colscheck']"));
		for(WebElement element:checkboxes){
			element.click();
		}
		driver.findElement(By.xpath("//div[@id='popupModal']//a[contains(text(),'OK')]")).click();
		delay(2000);
	}
	
	public static void selectOptions(){
		List<WebElement> selectboxes = driver.findElements(By.xpath("//div[@id='popupModal']//select"));
		for(int i=0; i<selectboxes.size(); i++){
			Select select = new Select(selectboxes.get(i));
			select.selectByIndex(i+1);
		}
		driver.findElement(By.xpath("//div[@id='popupModal']//a[contains(text(),'OK')]")).click();
		delay(2000);
	}
	
	public static void dragAndDropElement(WebElement dragFrom, WebElement dragTo, int xOffset)
			throws Exception {
			        //Setup robot
			        Robot robot = new Robot();
			        robot.setAutoDelay(50);

			        //Fullscreen page so selenium coordinates work
			        robot.keyPress(KeyEvent.VK_F11);
			        Thread.sleep(2000);

			        //Get size of elements
			        Dimension fromSize = dragFrom.getSize();
			        Dimension toSize = dragTo.getSize();

			        //Get centre distance
			        int xCentreFrom = fromSize.width / 2;
			        int yCentreFrom = fromSize.height / 2;
			        int xCentreTo = toSize.width / 2;
			        int yCentreTo = toSize.height / 2;

			        //Get x and y of WebElement to drag to
			        Point toLocation = dragTo.getLocation();
			        Point fromLocation = dragFrom.getLocation();

			        //Make Mouse coordinate centre of element
			        toLocation.x += xOffset + xCentreTo;
			        toLocation.y += yCentreTo;
			        fromLocation.x += xCentreFrom;
			        fromLocation.y += yCentreFrom;

			        //Move mouse to drag from location
			        robot.mouseMove(fromLocation.x, fromLocation.y);

			        //Click and drag
			        robot.mousePress(InputEvent.BUTTON1_MASK);

			        //Drag events require more than one movement to register
			        //Just appearing at destination doesn't work so move halfway first
			        robot.mouseMove(((toLocation.x - fromLocation.x) / 2) + fromLocation.x, ((toLocation.y
			- fromLocation.y) / 2) + fromLocation.y);

			        //Move to final position
			        robot.mouseMove(toLocation.x, toLocation.y);

			        //Drop
			        robot.mouseRelease(InputEvent.BUTTON1_MASK);
			        robot.keyPress(KeyEvent.VK_F11);
			    }
	
	public static void switchToTab() {
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());

		// Switch to new window
		driver.switchTo().window(tabs.get(1));
		// driver.close();//do some action in new window(2nd tab)
		System.out.println("Switched to " + driver.getTitle() + " tab");

	}
	
	public static void testBos(){
		WebElement menuFile = driver.findElement(By.xpath("//div[@id='north']//a[contains(text(),'Test')]"));
		WebElement optionTest = driver.findElement(By.xpath("//div[@id='north']//a[contains(text(),'Test/Execute')]"));
		
		menuFile.click();
		delay(1000);
		optionTest.click();
		delay(1000);
		
		WebElement testBtn = driver.findElement(By.xpath("//div[@id='popupModal']//a[contains(text(),'Test')]"));
		WebElement cancelBtn = driver.findElement(By.xpath("//div[@id='popupModal']//a[contains(text(),'Cancel')]"));
		testBtn.click();
		delay(5000);
		cancelBtn.click();
	}

	public static void main(String[] args) throws FindFailed {
		
	try {
		//System.out.println(screen.getNumberScreens());
		//System.out.println(screen.getBounds());
		startDriver();
		signIn();
		launchBuilder();
		openMyToolboxTab();
		addSqlAction();
		configureSQLSelectProp();
		bosMapping();
		testBos();		
		stopDriver();
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	}

}
