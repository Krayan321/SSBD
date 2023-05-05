package pl.lodz.p.it.ssbd2023.ssbd01.util.email;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import jakarta.inject.Inject;
import lombok.extern.java.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class EmailService {

    private ClientOptions options;

    private MailjetClient client;

    /*@ConfigProperty(name = "mailjet.key")
    private String MAILJET_KEY;

    @ConfigProperty(name = "mailjet.secret")
    private String MAILJET_SECRET;*/
    //todo secret to config

    public EmailService() {
        options = ClientOptions.builder()
                .apiKey("e3ae3249248fece726bdbc7ba42c2d8e")
                .apiSecretKey("d917e11da8837b0db091dfaab088663e")
                .build();

        client =  new MailjetClient(options);
    }

    public String sendPussyEmail(String email, String name) throws MailjetException {
        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "turbokozakapteka@proton.me")
                                        .put("Name", "PussyDealer"))
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

