import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import LinkedIn.GenerateGoogleAuthenticatorOTP as GoogleAuthenticator

// Thông tin đăng nhập LinkedIn của bạn
String email = 'hungnguyenbaophuc04@gmail.com'

String encryptedPassword = 'Phuchung2301'

// Khóa bí mật từ Google Authenticator
String secretKey = '7BUBPAW77DE2ATUU2YKHBP5QJV2ZNAEU'

try {
    // Mở trang chủ LinkedIn
    WebUI.openBrowser('')

    WebUI.navigateToUrl('https://www.linkedin.com/')

    WebUI.click(findTestObject('Page_LinkedIn Log In or Sign Up/a_Sign in'))

    // Nhập email và mật khẩu
    WebUI.setText(findTestObject('Page_LinkedIn Login, Sign in  LinkedIn/txtbox_Email'), email)

    WebUI.delay(3)

    WebUI.setText(findTestObject('Page_LinkedIn Login, Sign in  LinkedIn/txtboxPassword'), encryptedPassword)

    WebUI.delay(3)

    WebUI.click(findTestObject('Page_LinkedIn Login, Sign in  LinkedIn/button_Sign in'))

    // Sinh mã OTP bằng keyword tùy biến
    String otpCode = GoogleAuthenticator.generateOTP(secretKey)

    // Nhập mã OTP và gửi
    WebUI.setText(findTestObject('Page_Security Verification  LinkedIn/txtboxCode'), otpCode)

    WebUI.click(findTestObject('Page_Security Verification  LinkedIn/button_Submit'))

    // Kiểm tra phản hồi sau khi nhập OTP
    boolean notificationsPresent = WebUI.verifyElementPresent(findTestObject('Page_Feed  LinkedIn/Page_Feed  LinkedIn/a_Home'), 
        5, FailureHandling.OPTIONAL)

    if (notificationsPresent) {
        WebUI.comment('Đăng nhập thành công và chuyển đến trang chủ.')
    } else {
        boolean otpErrorPresent = WebUI.verifyElementPresent(findTestObject('Page_Security Verification  LinkedIn/OTPError'), 
            5, FailureHandling.OPTIONAL)

        if (otpErrorPresent) {
            WebUI.comment('Mã OTP sai.')
        } else {
            WebUI.comment('Có lỗi xảy ra hoặc không xác định được trạng thái hiện tại.')
        }
    }
}
catch (Exception e) {
    WebUI.comment('Có lỗi xảy ra: ' + e.getMessage())
} 
finally { 
    // Đóng trình duyệt
    WebUI.closeBrowser()
}

