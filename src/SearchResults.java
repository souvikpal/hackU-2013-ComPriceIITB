
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchResults extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** 
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		try {
			String type = request.getParameter("type");
			String query = request.getParameter("query").toLowerCase();
			String genspec = request.getParameter("genspec");
			int pricefrom = Integer.parseInt(request.getParameter("pricefrom"));
			int priceto = Integer.parseInt(request.getParameter("priceto"));

			if (pricefrom==-1){
				pricefrom=0;
			}
			if (priceto==-1){
				priceto=Integer.MAX_VALUE;
			}
			if(pricefrom>=priceto){
				priceto=Integer.MAX_VALUE;
			}
			System.out.println("Showing Results for: " + query + " in " + query + " category");
			System.out.println("The price range is from " + pricefrom + " to " + priceto);

			ComPrice cp = new ComPrice(query, type);
			cp.fetchResult();

			ArrayList<ArrayList<SearchObject>>finalMatchList = new ArrayList<ArrayList<SearchObject>>();



			if(genspec.equals("specific")) {

				/* Search Flipkart */

				int max=0,i,count = 0;
				SearchObject temp=null;

				ArrayList<String>q=new ArrayList<String>(Arrays.asList(query.split("[ +]")));
				int qLen = q.size();
				ArrayList<SearchObject>results=new ArrayList<SearchObject>();
				i=0;

				ArrayList<SearchObject> matchList = new ArrayList<>(); // added new

				for(SearchObject fp : cp.flipkart.FlipkartObjects) {

					ArrayList<String>objectname=new ArrayList<String>(Arrays.asList(fp.getName().split("[ ./)(,]")));
					objectname.removeAll(Arrays.asList("", null));
					count=0;
					if(i<5){

						HashSet<String> HS = new HashSet<String>();
						HS.addAll(objectname);
						objectname.clear();
						objectname.addAll(HS);


						for(String subq : q){
							for(String subo: objectname){
								if(subo.toLowerCase().contains(subq.toLowerCase())){
									count++;
								}
							}
						}
						if(count > max){
							max=count;
							temp=fp;
							matchList.clear();		// added later
							matchList.add(fp);		// adder later
							//System.out.println("Flipkart Removing and Adding: " + fp.getName() + " with length " + qLen);
						}
						else if(count == max) {
							if(matchList.size() < 2)
								matchList.add(fp); 		// added later
							//System.out.println("Flipkart Adding: " + fp.getName() + " with length " + qLen);
						}
						else
							;
					}
					else
						break;
					i++;
				}

				//System.out.println("Flipkart: " + max + " " + qLen);


				if (qLen <= max) {

					for(SearchObject temp1 : matchList) {


						//System.out.println("Searching other sites with: " + temp1.getName());

						temp1.setSource("Flipkart");
						results.add(temp1);

						ArrayList<String>fname = new ArrayList<String>(Arrays.asList(temp1.getName().split("[ ./)(,]")));
						fname.removeAll(Arrays.asList("", null));
						SearchObject match=null;
						max=0;

						for(SearchObject eb : cp.ebay.EbayObjects)
						{
							ArrayList<String>objectname=new ArrayList<String>(Arrays.asList(eb.getName().split("[ ./)(,]")));
							objectname.removeAll(Arrays.asList("", null));
							ArrayList<String>checkList = new ArrayList<String>();
							count=0;
							for(String subo: objectname) {
								for(String name : fname) {
									if(checkList.contains(subo) || checkList.contains(name))
										continue;
									if(name.toLowerCase().contains(subo.toLowerCase()) || subo.toLowerCase().contains(name.toLowerCase())) {
										count++;
										checkList.add(name);
										checkList.add(subo);
										//System.out.println(name + " = " + subo);
									}
								}
							}
							if(count>max) {
								max=count;
								match=eb;
								//System.out.println("In ebay: " + match.getName() + " " + max);
								//System.out.println(match);
							}
						}
						count = 0;
						ArrayList<String>objectname=new ArrayList<String>(Arrays.asList(match.getName().split("[ ./)(,]")));
						for (String subq : q) {
							for(String name : objectname)
								if(name.toLowerCase().contains(subq.toLowerCase())){
									count++;
								}

						}
						//System.out.println("Ebay: " + count + " : " + qLen );
						if(count >= qLen) {
							match.setSource("Ebay");
							results.add(match);
							//System.out.println("Ebay: " + count + " : " + qLen );
							//							cp.ebay.EbayObjects.remove(match);
						}


						max = 0;
						for(SearchObject infi : cp.infibeam.InfibeamObjects)
						{
							ArrayList<String>objectname1=new ArrayList<String>(Arrays.asList(infi.getName().split("[ ./)(,]")));
							objectname1.removeAll(Arrays.asList("", null));
							ArrayList<String>checkList = new ArrayList<String>();
							count=0;
							for(String subo: objectname1) {
								for(String name : fname) {
									if(checkList.contains(subo) || checkList.contains(name))
										continue;
									if(name.toLowerCase().contains(subo.toLowerCase()) || subo.toLowerCase().contains(name.toLowerCase())) {
										count++;
										checkList.add(name);
										checkList.add(subo);
										//System.out.println(name + " = " + subo);
									}
								}
							}
							if(count>max){
								max=count;
								match=infi;
								//System.out.println("In infi: " + match.getName() + " " + max);
							}
						}
						count = 0;
						ArrayList<String>objectname1=new ArrayList<String>(Arrays.asList(match.getName().split("[ ./)(,]")));

						for (String subq : q) {
							for(String name : objectname1)
								if(name.toLowerCase().contains(subq.toLowerCase())){
									count++;
								}

						}
						//System.out.println("Infi: " + count + " : " + qLen );
						if(count >= qLen) {
							match.setSource("Infibeam");
							results.add(match);
							//System.out.println("Infi Adding: " + match.getName() );
							//cp.infibeam.InfibeamObjects.remove(match);
						}
						ArrayList<SearchObject>NewTemp = new ArrayList<SearchObject>(results);
						finalMatchList.add(NewTemp);
						//System.out.println("FINAL MATCH LISt SIZE: " + finalMatchList.size() + "ALL: "  + finalMatchList.toString());
						//System.out.println("List: +++++++++++++++++  " + finalMatchList.get(index).get(0).getName() + " " + finalMatchList.get(index++).get(1).getName());
						results.clear();
						//System.out.println("FINAL MATCH LISt SIZE fffff: " + finalMatchList.size() + "ALL: "  + finalMatchList.toString());

					}
					//System.out.println("FINAL MATCH LISt SIZE adasdasdas: " + finalMatchList.size() + "ALL: "  + finalMatchList.toString());	

				}






				// Flipkart Search failed. Searching ebay for exact match

				else // Search Ebay 
				{
					System.out.println("Flipkart Search Failed.");

					ArrayList<SearchObject> matchList1 = new ArrayList<>();

					i=0; max=0;
					temp=null;
					for(SearchObject eb : cp.ebay.EbayObjects){
						ArrayList<String>objectname=new ArrayList<String>(Arrays.asList(eb.getName().split("[ ./)(,]")));
						objectname.removeAll(Arrays.asList("", null));
						count=0;
						if(i<5){

							HashSet<String> HS = new HashSet<String>();
							HS.addAll(objectname);
							objectname.clear();
							objectname.addAll(HS);

							for(String subq : q){
								for(String subo: objectname){
									if(subo.toLowerCase().contains(subq.toLowerCase())){
										count++;
									}
								}
							}
							if(count>max){
								max=count;
								temp=eb;
								matchList1.clear();		// added later
								matchList1.add(eb);		// adder later
								//System.out.println("Ebay " + count);
							}
							else if(count == max) {
								if(matchList1.size() < 2)
									matchList.add(eb); 		// added later
								//System.out.println("Flipkart Adding: " + fp.getName() + " with length " + qLen);
							}
							else
								;
						}
						else 
							break;
						i++;
					}
					if(qLen <= max) {
						for(SearchObject temp1 : matchList1) {
							//System.out.println("Searching  sites with: " + temp1.getName());

							temp1.setSource("Ebay");
							results.add(temp1);

							ArrayList<String>fname=new ArrayList<String>(Arrays.asList(temp.getName().split("[ ./)(,]")));
							fname.removeAll(Arrays.asList("", null));

							SearchObject match=null;

							max = 0;
							for(SearchObject infi : cp.infibeam.InfibeamObjects)
							{
								ArrayList<String>objectname1=new ArrayList<String>(Arrays.asList(infi.getName().split("[ ./)(,]")));
								objectname1.removeAll(Arrays.asList("", null));
								ArrayList<String>checkList = new ArrayList<String>();
								count=0;
								for(String subo: objectname1) {
									for(String name : fname) {
										if(checkList.contains(subo) || checkList.contains(name))
											continue;
										if(name.toLowerCase().contains(subo.toLowerCase()) || subo.toLowerCase().contains(name.toLowerCase())) {
											count++;
											checkList.add(name);
											checkList.add(subo);
											//System.out.println(name + " = " + subo);
										}
									}
								}
								if(count>max){
									max=count;
									match=infi;
									//System.out.println("In infi: " + match.getName() + " " + max);
								}
							}
							count = 0;
							ArrayList<String>objectname1=new ArrayList<String>(Arrays.asList(match.getName().split("[ ./)(,]")));

							for (String subq : q) {
								for(String name : objectname1)
									if(name.toLowerCase().contains(subq.toLowerCase())){
										count++;
									}

							}
							//System.out.println("Infi: " + count + " : " + qLen );
							if(count >= qLen) {
								match.setSource("Infibeam");
								results.add(match);
								//System.out.println("Infi Adding: " + match.getName() );
								//cp.infibeam.InfibeamObjects.remove(match);
							}
							ArrayList<SearchObject>NewTemp = new ArrayList<SearchObject>(results);
							finalMatchList.add(NewTemp);
							results.clear();
							//System.out.println("FINAL MATCH LISt SIZE: " + finalMatchList.size() + "ALL: "  + finalMatchList.toString());

							//System.out.println("List: +++++++++++++++++  " + finalMatchList.get(index).get(0).getName() + " " + finalMatchList.get(index++).get(1).getName());
							//System.out.println("FINAL MATCH LISt SIZE fffff: " + finalMatchList.size() + "ALL: "  + finalMatchList.toString());
						}
					}




					else {
						//Search Infibeam
						i=0; max=0;
						temp=null;
						for(SearchObject infi : cp.infibeam.InfibeamObjects){
							ArrayList<String>objectname=new ArrayList<String>(Arrays.asList(infi.getName().split("[ ./)(,]")));
							objectname.removeAll(Arrays.asList("", null));
							count=0;
							if(i<5){

								HashSet<String> HS = new HashSet<String>();
								HS.addAll(objectname);
								objectname.clear();
								objectname.addAll(HS);

								for(String subq : q){
									for(String subo: objectname){
										if(subo.toLowerCase().contains(subq.toLowerCase())){
											count++;
										}
									}
								}
								if(count>max){
									max=count;
									temp=infi;
									//System.out.println(count);
								}
							}
							else 
								break;
							i++;
						}
						if(qLen <= max) {
							temp.setSource("Infibeam");
							results.add(temp);
						}
					}
				}
				int size = finalMatchList.size();
				//out.println("<h1 id=headsup>Best Deal</h1>");
				for(ArrayList<SearchObject> results1 : finalMatchList) {
					int index=0;
					if(size > 0) {
						SearchObject temp1 = results1.get(index);
						out.println("<table height=\"300\" width=\"900\" border=0 cellpadding=5><tr><td rowspan=\"" + 4 +"\">" +
								"<img align=\"center\" height=\"200\" src=\"" + temp1.getImgurl() +
								"\" /></td>" +
								"<td> <font size=\"4\"> <b>" + temp1.getName() + "</b></font></td></tr>");


						for(SearchObject s:results1) {
							out.println("<tr> <td colspan=\"2\"><a href=\"" + s.getLink() + "\"> <font size=\"6\">" + s.getSource() + 
									": Rs. <b>" + s.getPrice() +"&#47;-</b></font></a></td></tr>");



						}
						out.println("</table>");
					}					
					else {
						out.println("<table height=\"300\" width=\"900\" border=0 cellpadding=5>" +
								"<tr><td>" + "<font size=\"6\">Sorry No Result Found</font>" + "</td></tr>" +
								"<tr><td>" + "<font size=\"6\">Please try General Search</font>" + "</td></tr>" +
								"</table>");
					}
				}
			}
			else {
				// Generic Search

				int i=0;
				ArrayList<String>q=new ArrayList<String>(Arrays.asList(query.split(" ")));

				/*				out.println("<head><title> <div class=\"title\">Search Result </div></title> </head>");*/
				out.println("<body>");
				out.println("<p>");
				/*				out.println("<h1 id=headsupothers>Search Results:</h1>");*/

				out.println("<div id=\"container\" >");

				out.println("<table border=0>");
				out.println("<tr><th align=\"center\"><img src=\"images/Flipkart.jpg\" width=\"100\" /></th><th align=\"center\"><img src=\"images/ebay.jpg\" width=\"100\" /></th><th align=\"center\"><img src=\"images/infibeam.jpg\" width=\"100\" /></th></tr>");
				out.println("<tr valign=top>");
				out.println("<td >");
				out.println("<div id=\"flipkart\"> ");
				out.println("<table border=0 width=\"300\">");

				i=0;
				int flag=0;
				for(SearchObject fp : cp.flipkart.FlipkartObjects) {
					if(fp.getPrice()>pricefrom && fp.getPrice()<priceto){
						ArrayList<String>objectname=new ArrayList<String>(Arrays.asList(fp.getName().toLowerCase().split("[ ./)(,]")));
						objectname.removeAll(Arrays.asList("", null));
						String newQuery = query;
						for(String oName : objectname) {

							if(!q.contains(oName)) {
								System.out.print(q+" "+oName + " ");
								flag = 0;
								for (String qq : q) {
									if(oName.contains(qq) || qq.contains(oName)) {
										double ratio = qq.length() > oName.length() ? (double)oName.length()/qq.length() : (double)qq.length()/oName.length();
										if (ratio >= 0.5) {
											flag=-1;
											break;
										}
										else {
											newQuery = query + " " + oName;
											flag = 2;
											break;
										}
									}
								}
								if(flag == -1)
									continue;
								else if (flag == 2)
									break;
								newQuery = query + " " + oName;
								break;
								//								System.out.println(newQuery);
							}
						}
						System.out.println();
						out.println("<tr align=\"center\"> <td><img src=\"" + fp.getImgurl() + 
								"\" height= \"150\" /> <br/> <b>" +	fp.getName() + "</b><br /> <font size=5> Rs. <b> "+ fp.getPrice() + "</font>&#47;- </b><br/></div>" +
								"<button name=\"simsearch\" class=\"simsearch\" value=\"" + newQuery + "\">Find Similar</button></div></div></td></tr>");
						i++;
					}
					else break;
				}
				//out.println("</tr>");
				out.println("</table>");
				out.println("</div>");
				out.println("</td>");
				out.println("<td>");
				out.println("<div id=\"eb\" >");
				out.println("<table border=0 width=\"300\">");



				i=0;

				for(SearchObject eb : cp.ebay.EbayObjects) {
					if(eb.getPrice()>pricefrom && eb.getPrice()<priceto){
						ArrayList<String>objectname=new ArrayList<String>(Arrays.asList(eb.getName().toLowerCase().split("[ ./)(,]")));
						objectname.removeAll(Arrays.asList("", null));
						String newQuery = query;
						for(String oName : objectname) {
							if(!q.contains(oName)) {
								flag = 0;
								for (String qq : q) {
									System.out.println("q: " + qq + " oname: " + oName);
									if(oName.contains(qq) || qq.contains(oName)) {
										double ratio = qq.length() > oName.length() ? (double)oName.length()/qq.length() : (double)qq.length()/oName.length();
										System.out.println("q: " + qq + " oname: " + oName + " ratio: " + ratio);
										if (ratio >= 0.5) {
											flag=-1;
											break;
										}
										else {
											newQuery = query + " " + oName;
											flag = 2;
											break;
										}
									}
								}
								if(flag == -1)
									continue;
								else if (flag == 2)
									break;
								newQuery = query + " " + oName;
								break;
								//								System.out.println(newQuery);
							}
						}
						out.println("<tr align=\"center\"> <td><img src=\"" + eb.getImgurl() + 
								"\" height= \"120\" /> <br/> <b>" +	eb.getName() + "</b><br /> <font size=5> Rs. <b> "+ eb.getPrice() + "</font>&#47;- </b><br/></div>" +
								"<button name=\"simsearch\" class=\"simsearch\" value=\"" + newQuery + "\">Find Similar</button></div></div></td></tr>");
						i++;
					}
					else
						break;
				}

				out.println("</table>");
				out.println("</div>");
				out.println("</td>");

				out.println("<td>");
				out.println("<div id=\"jb\" >");
				out.println("<table border=\"0\" width=\"300\">");


				i=0;
				for(SearchObject infi : cp.infibeam.InfibeamObjects) {
					if(infi.getPrice()>pricefrom && infi.getPrice()<priceto){ 
						ArrayList<String>objectname=new ArrayList<String>(Arrays.asList(infi.getName().toLowerCase().split("[ ./)(,]")));
						objectname.removeAll(Arrays.asList("", null));
						String newQuery = query;
						for(String oName : objectname) {
							if(!q.contains(oName)) {
								flag = 0;
								for (String qq : q) {

									if(oName.contains(qq) || qq.contains(oName)) {
										double ratio = qq.length() > oName.length() ? (double)oName.length()/qq.length() : (double)qq.length()/oName.length();

										if (ratio >= 0.5) {
											flag=-1;
											break;
										}
										else {
											newQuery = query + " " + oName;
											flag = 2;
											break;
										}
									}
								}
								if(flag == -1)
									continue;
								else if (flag == 2)
									break;
								newQuery = query + " " + oName;
								break;
								//								System.out.println(newQuery);
							}
						}
						//System.out.println("NewQuery = " + newQuery);

						out.println("<tr align=\"center\"> <td><img src=\"" + infi.getImgurl() + 
								"\" height= \"150\" /> <br/> <b>" +	infi.getName() + "</b><br /> <font size=5> Rs. <b> "+ infi.getPrice() + "</font>&#47;- </b><br/></div>" +
								"<button name=\"simsearch\" class=\"simsearch\" value=\"" + newQuery + "\">Find Similar</button></div></div></td></tr>");
						i++;
					}
					else break;
				}

				out.println("</table>");
				out.println("</div>");
				out.println("</td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</div>");
				out.println("</p>");
				out.println("</body>");
			}
		}
		finally {
			out.close();
		}
	}

	/** 
	 * Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/** 
	 * Handles the HTTP <code>POST</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/** 
	 * Returns a short description of the servlet.
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}
}
