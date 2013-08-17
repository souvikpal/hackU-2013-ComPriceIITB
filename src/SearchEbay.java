import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class SearchEbay extends Thread {
	private String finalQuery;
	private String type;
	ArrayList<SearchObject>EbayObjects = new ArrayList<SearchObject>();

	SearchEbay(String finalQuery, String objType) {
		this.finalQuery = finalQuery;
		this.type = objType;
	}

	int attempts = 0;
	public void run() {
		while (true) {
			try {
				Document docFlip = Jsoup.connect(finalQuery).get();
				String docTitle = docFlip.title();
				System.out.print("Searching Ebay... ");
				System.out.println(docTitle);
				String Name, Price, Imgurl, ProdUrl=null;
				int price;
				Elements ebayWatches = docFlip.select("table[itemtype]");
				for (Element link : ebayWatches) {
					Name = link.select("img[itemprop]").attr("alt");
					Imgurl = link.select("img[itemprop]").attr("src");
					Price = link.select("div[itemprop=price]").text().replaceAll("Rs. ", "").replaceAll(",", "");
					price = Integer.parseInt(Price.substring(0, Price.indexOf(".")));
					ProdUrl = link.select("a[itemprop=url]").attr("href");
					SearchObject obj = new SearchObject(Name, price, Imgurl, type, ProdUrl);
					EbayObjects.add(obj);
				}
				
				break;
			} catch (IOException e) {
				System.out.println("Ebay Connection Failed. Retrying...");
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