package guru.qa.niffler.api;

import guru.qa.niffler.model.*;

import java.io.IOException;
import java.util.List;

public class GatewayApiClient extends ApiClient {

    private GatewayApi gatewayApi;

    public GatewayApiClient() {
        super(CFG.gatewayUrl());
        this.gatewayApi = retrofit.create(GatewayApi.class);
    }

    public List<CategoryJson> getCategories(String bearerToken) throws IOException {

        return gatewayApi
                .getCategories(bearerToken)
                .execute()
                .body();
    }

    public CategoryJson addCategory(String bearerToken, CategoryJson category) throws IOException {
        return gatewayApi
                .addCategory(bearerToken, category)
                .execute()
                .body();
    }

    public List<CurrencyJson> getAllCurrencies(String bearerToken) throws IOException {
        return gatewayApi
                .getAllCurrencies(bearerToken)
                .execute()
                .body();
    }

    public List<UserJson> friends(String bearerToken) throws IOException {
        return gatewayApi
                .friends(bearerToken)
                .execute()
                .body();
    }

    public void removeFriend(String bearerToken, String targetUsername) throws IOException {
        gatewayApi.removeFriend(bearerToken, targetUsername).execute();
    }

    public List<UserJson> incomeInvitations(String bearerToken) throws IOException {
        return gatewayApi
                .incomeInvitations(bearerToken)
                .execute()
                .body();
    }

    public List<UserJson> outcomeInvitations(String bearerToken) throws IOException {
        return gatewayApi
                .outcomeInvitations(bearerToken)
                .execute()
                .body();
    }

    public UserJson sendInvitation(String bearerToken, FriendJson friend) throws IOException {
        return gatewayApi
                .sendInvitation(bearerToken, friend)
                .execute()
                .body();
    }

    public UserJson acceptInvitation(String bearerToken, FriendJson invitation) throws IOException {
        return gatewayApi
                .acceptInvitation(bearerToken, invitation)
                .execute()
                .body();
    }

    public UserJson declineInvitation(String bearerToken, FriendJson invitation) throws IOException {
        return gatewayApi
                .declineInvitation(bearerToken, invitation)
                .execute()
                .body();
    }

    public SessionJson session(String bearerToken) throws IOException {
        return gatewayApi
                .session(bearerToken)
                .execute()
                .body();
    }

    public List<SpendJson> getSpends(String bearerToken) throws IOException {
        return gatewayApi
                .getSpends(bearerToken)
                .execute()
                .body();
    }

    public SpendJson addSpend(SpendJson spend, String bearerToken) throws IOException {
        return gatewayApi
                .addSpend(spend, bearerToken)
                .execute()
                .body();
    }

    public SpendJson editSpend(SpendJson spend, String bearerToken) throws IOException {
        return gatewayApi
                .editSpend(spend, bearerToken)
                .execute()
                .body();
    }

    public void deleteSpends(String bearerToken, List<String> ids) throws IOException {
        gatewayApi
                .deleteSpends(bearerToken, ids)
                .execute();
    }

    public List<StatisticJson> getTotalStatistic(String bearerToken) throws IOException {
        return gatewayApi
                .getTotalStatistic(bearerToken)
                .execute()
                .body();
    }

    public UserJson currentUser(String bearerToken) throws IOException {
        return gatewayApi
                .currentUser(bearerToken)
                .execute()
                .body();
    }

    public List<UserJson> allUsers(String bearerToken) throws IOException {
        return gatewayApi
                .allUsers(bearerToken)
                .execute()
                .body();
    }

    public UserJson updateUserInfo(String bearerToken, UserJson user) throws IOException {
        return gatewayApi
                .updateUserInfo(bearerToken, user)
                .execute()
                .body();
    }
}
