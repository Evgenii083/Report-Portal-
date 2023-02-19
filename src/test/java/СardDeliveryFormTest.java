import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Keys;
import utils.DataGenerator;
import utils.LoggingUtils;
import utils.ScreenShooterReportPortalExtension;

import java.time.Duration;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@ExtendWith(ScreenShooterReportPortalExtension.class)

public class СardDeliveryFormTest {


    @BeforeEach
    void setUp() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1000x900";
        open("http://localhost:9999/");
    }


    @Test
    public void happyPathWithoutReplanningMeeting() {
        int daysToAddFirstMeeting = 5;
        String firstMeetingDay = DataGenerator.generateData(daysToAddFirstMeeting, "dd.MM.yyyy");
        int daysToAddSecondMeeting = 7;
        String secondMeetingDay = DataGenerator.generateData(daysToAddSecondMeeting, "dd.MM.yyyy");
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");

        $x("//input[contains (@placeholder, 'Город' )]").setValue(validUser.getCity());
        LoggingUtils.logInfo("В поле ввода введен валидный город " + validUser.getCity());
        $x("//* [contains(@placeholder , 'Дата встречи')]")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $x("//*[contains(@placeholder , 'Дата встречи')]").setValue(firstMeetingDay);
        LoggingUtils.logInfo("В поле ввода введена валидная дата " + firstMeetingDay);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        LoggingUtils.logInfo("В поле ввода введено валидное имя " + validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        LoggingUtils.logInfo("В поле ввода введен валидный номер телефона " + validUser.getPhone());
        $x("//label[contains(@data-test-id, 'agreement')]").click();
        LoggingUtils.logInfo("Клик по чек-боксу ");
        $x("//*[contains(text(),'Запланировать')]").click();
        LoggingUtils.logInfo("Клик по кнопке 'Запланировать' ");

        $x("//*[contains(@class,'notification__content')]")
                .shouldBe(visible, Duration.ofMillis(3000))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDay));
        LoggingUtils.logInfo("Встреча запланирована");
        $x("//* [contains(@placeholder , 'Дата встречи')]")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $x("//*[contains(@placeholder , 'Дата встречи')]").setValue(secondMeetingDay);
        LoggingUtils.logInfo("Перепланируем встречу с новой датой " + secondMeetingDay);
        $x("//*[contains(text(), 'Запланировать')]").click();
        LoggingUtils.logInfo("Клик по кнопке 'Запланировать' ");
        $x("//*[contains(text(),'Необходимо подтверждение')]").shouldBe(appear, Duration.ofSeconds(3));
        $x("//div[contains(text(),'У вас уже запланирована встреча на другую дату. Перепланировать?')]")
                .shouldBe(appear, Duration.ofSeconds(3));
        LoggingUtils.logInfo("Подтверждаем изменение даты встречи ");
        $x("//span[contains(text(),'Перепланировать')]").click();
        $x("//*[contains(@class,'notification__content')]")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDay),
                        Duration.ofSeconds(5)).shouldBe(visible);
        LoggingUtils.logInfo("Встреча перепланирована");
    }

}
