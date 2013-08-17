import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class SearchInfibeam extends Thread {
	private String finalQuery;
	private String type;
	ArrayList<SearchObject>InfibeamObjects = new ArrayList<SearchObject>();

	SearchInfibeam(String finalQuery, String objType) {
		this.finalQuery = finalQuery;
		this.type = objType;
	}
	int attempts = 0;
	public void run() {
		while(true) {
			try {
				Document docBeam = Jsoup.connect(finalQuery).get();

				String docTitle = docBeam.title();
				System.out.print("Searching Infibeam... ");
				System.out.println(docTitle);

				String Name, Price, Imgurl, ProdUrl;
				int price;

				Elements infiLaptops = docBeam.getElementsByAttributeValue("class", "srch_result default").select("li");
				for (Element link : infiLaptops) {

					Name = link.select("a[href]").attr("title");
					Price = link.getElementsByAttributeValue("class", "price").text();
					if(Price.indexOf(" " ) > 0)
						Price  = Price.substring(0, Price.indexOf(" "));
					price = Integer.parseInt(Price.replaceAll(",", ""));
					Imgurl = link.select("img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
					ProdUrl = "http://www.infibeam.com" + link.select("a[href]").attr("href");
					SearchObject obj = new SearchObject(Name, price, Imgurl, type, ProdUrl);
					InfibeamObjects.add(obj);
				}
				break;

			} catch (IOException e) {
				System.out.println("Infibeam Connection Failed. Retrying...");
				attempts++;
				if(attempts > 10) {
					System.out.println("Could not connect in 10 attempts");
					break;
				}
			}
		}
	}
}
