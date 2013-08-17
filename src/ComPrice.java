import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ComPrice {

	SearchFlipkart flipkart;
	SearchEbay ebay;
	SearchInfibeam infibeam;

	ComPrice(String queryString, String type) {
		final String authUser = "11305R012";
		final String authPassword = "8PHKWfly@";
		Authenticator.setDefault(
				new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(authUser, authPassword.toCharArray());
					}
				}
				);
		System.setProperty("http.proxyHost","netmon.iitb.ac.in" );
		System.setProperty("http.proxyPort", "80");
		System.setProperty("http.proxyUser", authUser);
		System.setProperty("http.proxyPassword", authPassword);

		String wellFormedQuery = queryString.replaceAll(" ", "+");
		String flipQuery = null, ebayQuery = null, infibeamQuery = null;
		
		switch (type) {

		case "Mobile":
			//flipQuery = "http://www.flipkart.com/search?q=" + wellFormedQuery + "&as=off&as-show=off&otracker=start";
			flipQuery = "http://www.flipkart.com/mobiles/pr?p[]=facets.availability%255B%255D%3DExclude%2BOut%2Bof%2BStock&p[]=sort%3Drelevance&sid=tyy%2C4io&q=" + wellFormedQuery + "&as=off&as-show=off&otracker=start";
			ebayQuery = "http://www.ebay.in/sch/i.html?_trksid=p3902.m570.l1313.TR8.TRC0.A0.X" + wellFormedQuery + "&_nkw=" + wellFormedQuery+"&_sacat=15032&_from=R40";
			infibeamQuery = "http://www.infibeam.com/Mobiles/search?q=" + wellFormedQuery;
			break;

		case "Laptop":
			flipQuery = "http://www.flipkart.com/laptops/pr?p[]=facets.availability%255B%255D%3DExclude%2BOut%2Bof%2BStock&p[]=sort%3Drelevance&sid=6bo%2Cb5g&q=" + wellFormedQuery + "&as=off&as-show=off&otracker=start";
			//http://www.flipkart.com/laptops/pr?p[]=facets.availability%255B%255D%3DExclude%2BOut%2Bof%2BStock&p[]=sort%3Drelevance&sid=6bo%2Cb5g&q=sony+vaio
			ebayQuery = "http://www.ebay.in/sch/i.html?_trksid=p3902.m570.l1313.TR8.TRC0.A0.X" + wellFormedQuery + "&_nkw=" + wellFormedQuery+"&_sacat=160&_from=R40";
			infibeamQuery = "http://www.infibeam.com/Laptop_Computers_Accessories/search?q=" + wellFormedQuery;
			break;

		case "Watch":
			flipQuery = "http://www.flipkart.com/watches/wrist-watches/pr?p[]=facets.availability%255B%255D%3DExclude%2BOut%2Bof%2BStock&p[]=sort%3Drelevance&sid=r18%2Cf13&q=" + wellFormedQuery + "&as=off&as-show=off&otracker=start";
			ebayQuery = "http://www.ebay.in/sch/i.html?_trksid=p3902.m570.l1313.TR8.TRC0.A0.X" + wellFormedQuery + "&_nkw=" + wellFormedQuery+"&_sacat=14324&_from=R40";
			infibeamQuery = "http://www.infibeam.com/Watches/search?q=" + wellFormedQuery;
			break;
		default:
			break;

		}
		flipkart = new SearchFlipkart(flipQuery, type);
		ebay = new SearchEbay(ebayQuery, type);
		infibeam = new SearchInfibeam(infibeamQuery, type);
	}

	void fetchResult() {
		
		flipkart.start();
		ebay.start();
		infibeam.start();

		try {
			flipkart.join();
			ebay.join();
			infibeam.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}