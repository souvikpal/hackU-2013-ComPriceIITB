import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SearchFlipkart extends Thread {
	private String finalQuery;
	private String type;
	ArrayList<SearchObject>FlipkartObjects = new ArrayList<SearchObject>();

	int attempts = 0;
	SearchFlipkart(String finalQuery, String objType) {
		this.finalQuery = finalQuery;
		this.type = objType;
	}

	public void run() {
		while (true) {
			try {
				Document docFlip = Jsoup.connect(finalQuery).get();

				String docTitle = docFlip.title();
				System.out.print("Searching Flipkart... ");
				System.out.println(docTitle);

				String Name, Price, Imgurl, ProdUrl;
				int price;

				Elements flipWatches = docFlip.select("div[data-pid]");
				for (Element link : flipWatches) {

					Name = link.select("div[class=pu-title fk-font-13]").select("a[href]").attr("title");
					Price = link.select("div[class=pu-final]").text();
					try {
						price = Integer.parseInt(Price.replaceAll("Rs. ", ""));
					}
					catch (NumberFormatException ne) {
						price = 0;
						continue;
					}
					Imgurl = link.select("img[height]").attr("data-src");
					ProdUrl = "http://www.flipkart.com" + link.select("div[class=pu-title fk-font-13]").select("a[href]").attr("href");

					SearchObject obj = new SearchObject(Name, price, Imgurl, type, ProdUrl);
					FlipkartObjects.add(obj);
				}
				break;
			} catch (IOException e) {
				System.out.println("Flipkart Connection Failed. Retrying...");
				attempts++;
				if(attempts > 10) {
					System.out.println("Could not connect in 10 attempts");
					break;
				}
			}
		}
	}
}
