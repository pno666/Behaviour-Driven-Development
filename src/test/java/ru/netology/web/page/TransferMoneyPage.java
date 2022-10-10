package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class TransferMoneyPage {
    private SelenideElement amountField = $("[data-test-id=amount] input");
    private SelenideElement fromField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement errorTransfer = $("[data-test-id=error-notification]");

    public CardsPage transfer(int amount, String cardNumber) {
        amountField.setValue(Integer.toString(amount));
        fromField.setValue(cardNumber);
        transferButton.click();
        return new CardsPage();
    }

    public CardsPage getError() {
        $("#root").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("#root").shouldHave(Condition.exactText("Ошибка"));
        return new CardsPage();
    }
}
