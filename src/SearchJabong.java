/*import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class SearchJabong extends Thread {
	private String finalQuery;
	private String type;
	ArrayList<SearchObject>JabongObjects = new ArrayList<SearchObject>();
	
	SearchJabong(String finalQuery, String objType) {
		this.finalQuery = finalQuery;
		this.type = objType;
	}
	
	public void run() {
		try {
			Document docFlip = Jsoup.connect(finalQuery).get();

			String docTitle = docFlip.title();
			System.out.print("Searching... ");
			System.out.println(docTitle);

			String Name, Price, Imgurl;
			
			switch(type) {
			case "Watch": 
				Elements jabongWatches = docFlip.getElementsByAttributeValue("class","fleft pro-content");
				for (Element link : jabongWatches) {
					String temp = link.select("a[href]").attr("href").replace('-', ' ');
					Name = temp.substring(1, temp.lastIndexOf("."));
					Imgurl = link.select("img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
					Price = link.select("a[href]").select("strong").text().replaceAll("&nbsp;", " ");
					SearchObject obj = new SearchObject(Name, Price, Imgurl, "Watch");
					JabongObjects.add(obj);
				}
				break;


			case "Mobile": 
				Elements jabongMobiles = docFlip.getElementsByAttributeValue("class","fleft pro-content");
				for (Element link : jabongMobiles) {
					String temp = link.select("a[href]").attr("href").replace('-', ' ');
					Name = temp.substring(1, temp.lastIndexOf("."));
					Imgurl = link.select("img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
					Price = link.select("a[href]").select("strong").text().replaceAll("&nbsp;", " ");
					SearchObject obj = new SearchObject(Name, Price, Imgurl, "Watch");
					JabongObjects.add(obj);
				}
				break;


			case "Shoe": 
				Elements jabongShoes = docFlip.getElementsByAttributeValue("class","fleft pro-content");
				for (Element link : jabongShoes) {
					String temp = link.select("a[href]").attr("href").replace('-', ' ');
					Name = temp.substring(1, temp.lastIndexOf("."));
					Imgurl = link.select("img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
					Price = link.select("a[href]").select("strong").text().replaceAll("&nbsp;", " ");
					SearchObject obj = new SearchObject(Name, Price, Imgurl, "Watch");
					JabongObjects.add(obj);
				}
				break;
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}*/