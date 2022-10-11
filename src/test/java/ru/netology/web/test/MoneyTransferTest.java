package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.CardsPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferMoneyPage;

import static com.codeborne.selenide.Selenide.open;

public class MoneyTransferTest {
    @BeforeEach
    public void shouldLogin() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Transfer money from first card to second")
    public void shouldFromFirstToSecond() {
        var cardsPage = new CardsPage();
        var initBalanceFirst = cardsPage.getFirstCardBalance();
        var initBalanceSecond = cardsPage.getSecondCardBalance();
        int destinCardIndex = 1;
        int amount = 50;
        cardsPage.transfer(destinCardIndex)
                .transfer(amount, DataHelper.getFirstCardNumber().getNumber());
        var curBalanceFirst = cardsPage.getFirstCardBalance();
        var curBalanceSecond = cardsPage.getSecondCardBalance();
        Assertions.assertEquals(initBalanceFirst - amount, curBalanceFirst);
        Assertions.assertEquals(initBalanceSecond + amount, curBalanceSecond);

    }

    @Test
    @DisplayName("Transfer money from second card to first")
    public void shouldFromSecondToFirst() {
        var cardsPage = new CardsPage();
        var initBalanceFirst = cardsPage.getFirstCardBalance();
        var initBalanceSecond = cardsPage.getSecondCardBalance();
        int destinCardIndex = 0;
        int amount = 50;
        cardsPage.transfer(destinCardIndex)
                .transfer(amount, DataHelper.getSecondCardNumber().getNumber());
        var curBalanceFirst = cardsPage.getFirstCardBalance();
        var curBalanceSecond = cardsPage.getSecondCardBalance();
        Assertions.assertEquals(initBalanceFirst + amount, curBalanceFirst);
        Assertions.assertEquals(initBalanceSecond - amount, curBalanceSecond);
    }

    @Test
    @DisplayName("Get error massage with transfer from first card to second above balance")
    public void shouldAboveBalanceFromFirstToSecond() {
        var cardsPage = new CardsPage();
        var initBalanceFirst = cardsPage.getFirstCardBalance();
        var initBalanceSecond = cardsPage.getSecondCardBalance();
        int destinCardIndex = 1;
        int amount = 25_000;
        var mPage = cardsPage.transfer(destinCardIndex);
        mPage.transfer(amount, DataHelper.getFirstCardNumber().getNumber());
        mPage.getError();
        Assertions.assertEquals(10_000, initBalanceFirst);
        Assertions.assertEquals(10_000, initBalanceSecond);
    }

}
