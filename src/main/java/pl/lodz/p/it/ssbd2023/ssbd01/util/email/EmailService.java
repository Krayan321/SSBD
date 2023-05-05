package pl.lodz.p.it.ssbd2023.ssbd01.util.email;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import org.eclipse.microprofile.config.ConfigProvider;
import org.json.JSONArray;
import org.json.JSONObject;

public class EmailService {

    private ClientOptions options;

    private MailjetClient client;

    public EmailService() {
        options = ClientOptions.builder()
                .apiKey(ConfigProvider.getConfig().getValue("mailjet.key", String.class))
                .apiSecretKey(ConfigProvider.getConfig().getValue("mailjet.secret", String.class))
                .build();

        client =  new MailjetClient(options);
    }

    public String sendPussyEmail(String email, String name) throws MailjetException {
        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "turbokozakapteka@proton.me")
                                        .put("Name", "udalosie"))
                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", email)
                                                .put("Name", name)))
                .put(Emailv31.Message.SUBJECT, "Some good pussies for you!")
                                .put(Emailv31.Message.TEXTPART, "Dear PussyEnjoyer, welcome to PussyDelievery! May the pussy force be with you!")
                                .put(Emailv31.Message.HTMLPART, "<h3>Hello stranger, look at some pussies <a href=\"https://wallpapers.com/images/featured/y4upwj5zz45novpx.jpg:*/\">PussyDelievery</a>!</h3><br />May the pussy force be with you!")));

        return client.post(request).getData().toString();
    }
}

