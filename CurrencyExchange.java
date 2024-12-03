import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONObject;

//java -cp ".;lib\json-20210307.jar" CurrencyExchange
//javac -cp lib/json-20210307.jar CurrencyExchange.java

class APICall {

    String cur[] = { "INR", "USD", "EUR", "GBP", "GIP", "KWD", "BHD", "OMR", "JOD", "KYD", "CHF" };

    public void baseCurrency(String baseCurrency, double amount) {

        String url_str = "https://v6.exchangerate-api.com/v6/c387daa9c2167754fc8ee3dd/latest/" + baseCurrency;

        try {

            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();
            // System.out.println(request.getResponseCode());

            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonObj = new JSONObject(response.toString());
            JSONObject conversionRatesObject = jsonObj.getJSONObject("conversion_rates");

            Map<String, Object> map = conversionRatesObject.toMap();

            System.out.println("      BaseValue       ConvertedAmount");

            for (String str : cur) {
                if (!str.equals(baseCurrency)) {
                    BigDecimal bdm = (BigDecimal) map.get(str);
                    double val = (amount * bdm.doubleValue());
                    System.out.println(str + " : " + map.get(str) + " \t\t " + String.format("%.5f", val));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

public class CurrencyExchange {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        System.out.println("Currency Exchange Info");

        APICall obj = new APICall();

        int choice = 1;
        String newBase = "INR";
        do {
            switch (choice) {
                case 1:
                    System.out.println(
                            "Enter Base Currency : INR | USD | EUR |GBP | GIP | KWD | BHD | OMR | JOD | KYD | CHF ");
                    newBase = s.nextLine();
                    System.out.print("Enter amount to be converted : ");
                    double amount = s.nextDouble();

                    obj.baseCurrency(newBase, amount);

                    break;

                case 2:
                    System.out.print("Enter the new amount to be converted : ");
                    double newAmount = s.nextDouble();
                    obj.baseCurrency(newBase, newAmount);

                    break;

                case 3:
                    System.out.println("Exit...!");
                    return;

                default:
                    System.out.println("Invaild choice");
                    break;
            }

            System.out.println("-- -- -- -- -- -- Menu -- -- -- -- -- --");
            System.out.println(" 1.Change Base\n 2.Change amount\n 3.Exit");
            System.out.print("Enter choice : ");
            choice = s.nextInt();
            s.nextLine();

        } while (choice != 3);

        s.close();

    }
}