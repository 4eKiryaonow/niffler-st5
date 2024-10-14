package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.UserJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class UsersInTableCondition extends WebElementsCondition {

    private final UserJson[] expectedUsers;

    public UsersInTableCondition(UserJson[] expectedUsers) {
        this.expectedUsers = expectedUsers;
    }

    @Nonnull
    @Override
    public CheckResult check(Driver driver, List<WebElement> elements) {
        if (elements.size() != expectedUsers.length) {
            return CheckResult.rejected(
                    "Users table size mismatch",
                    elements.size()
            );
        }

        for (int i = 0; i < elements.size(); i++) {
            WebElement row = elements.get(i);
            UserJson expectedUserForRow = expectedUsers[i];

            List<WebElement> td = row.findElements(By.cssSelector("td"));

            boolean avatarResult;
            String photo = td.get(0).findElement(By.cssSelector("img")).getAttribute("src");
            String expectedPhoto = expectedUserForRow.photoSmall();

            if (photo.contains("images/niffler_avatar.jpeg")) {
                avatarResult = expectedPhoto == null;
            } else avatarResult = photo.equals(expectedPhoto);
            if (!avatarResult) {
                return CheckResult.rejected(
                        "User table: avatar mismatch",
                        photo
                );
            }

            String userName =  td.get(1).getText();
            boolean userNameResult = userName.equals(
                    expectedUserForRow.username()
            );
            if (!userNameResult) {
                return CheckResult.rejected(
                        "User table: userName mismatch",
                        userName
                );
            }

            boolean nameResult;
            String name = td.get(2).getText();
            if (name.isBlank()) {
                nameResult = expectedUserForRow.firstname() == null && expectedUserForRow.surname() == null;
            } else {
                nameResult = name.equals(expectedUserForRow.firstname() + " " + expectedUserForRow.surname());
            }
            if (!nameResult) {
                return CheckResult.rejected(
                        "User table: name mismatch",
                        name
                );
            }
        }
        return CheckResult.accepted();
    }

    @Override
    public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause, long timeoutMs) {
        List<WebElement> elements = collection.getElements();

        StringBuilder users = new StringBuilder();

        for (int j = 0; j < collection.getElements().size(); j++) {
            WebElement row = elements.get(j);
            List<WebElement> td = row.findElements(By.cssSelector("td"));
            users.append(String.format("%s | %s | %s | %s \n",
                    td.get(0).getText().isBlank() ? null : td.get(0).getText(),
                    td.get(1).getText(),
                    td.get(2).getText(),
                    getActions(td.get(3))
            ));
        }

        StringBuilder expectedUsersBuilder = new StringBuilder();
        for (int i = 0; i < expectedUsers.length; i++) {
            UserJson expectedUserForRow = expectedUsers[i];

            expectedUsersBuilder.append(String.format("%s | %s | %s | %s \n",
                    expectedUserForRow.photoSmall(),
                    expectedUserForRow.username(),
                    expectedUserForRow.surname(),
                    expectedUserForRow.friendState()

            ));
        }

        String message = lastCheckResult.getMessageOrElse(() -> "Spending mismatch");
        throw new SpendMismatchException(message,
                collection,
                expectedUsersBuilder.toString(),
                users.toString(),
                explanation,
                timeoutMs,
                cause);
    }

    @Override
    public String toString() {
        return null;
    }

    private String getActions(WebElement row) {
        return row.getText().isBlank() ?
                row.findElement(By.cssSelector("div[data-tooltip-id]")).getAttribute("data-tooltip-content")
                : row.getText();
    }
}