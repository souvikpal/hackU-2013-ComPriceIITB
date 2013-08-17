<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>Compare Prices of Familiar Products</title>

<link href="stylesheets/default.css" rel="stylesheet" type="text/css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script>
$(document).on("click", '.simsearch', function() {
	  $("#query").val($(this).val());
	  $("#hitme").trigger('click');
	});
	$(document).ready(
			function() {
				$("#detdiv").hide();
				$('#hitme').click(
						function(event) {
							$.ajax({
								type : "POST",
								url : "SearchResults",
								data : "type=" + $("#type").val() + "&query="
										+ $("#query").val()+ "&genspec="
										+ $(".genspec:checked").val() + "&pricefrom=" + $("#pricefrom").val() + "&priceto=" + $("#priceto").val(),
								success : function(data) {
									//alert("pricefrom=" + $("#pricefrom").val() + "priceto=" + $("#priceto").val());
									$("#post").html(data);
									//$(".infobox").hide();
								}
							});
							$("#query").attr("value",null);
						});

				$('#homepage').click(function(event) {

						$('#post').html("<h2 class=\"title\">Search Products. Compare Prices.</h2><h4>Watch The Magic Happen Here!</h4>");

				});

				$('#madness').click(function(event) {
					$(".itembox").children("div").toggle();
				});


				$("input[value=general]").click(function(event) {
					$("#detdiv").show(1000);
				});
				$("input[value=specific]").click(function(event) {
					$("#detdiv").hide(1000);
					$("[name=pricefrom]").val(-1);
					$("[name=priceto]").val(-1);
				});
				$('#about').click(function(event) {
					$.get('AboutUs', function(responseText) {
						$('#post').html(responseText);
					});
				});
				$('#contact').click(function(event) {
					$.get('ContactUs', function(responseText) {
						$('#post').html(responseText);
					});
				});
			});
</script>

</head>
<body>
	<!-- start header -->
	<div id="header">
		<div id="menu">
			<ul>
				<li class="current_page_item" id="homepage"><a href="#">Homepage</a></li>
				<li class="about" id="about"><a href="#">About</a></li>
				<li class="contact" id="contact"><a href="#">Contact</a></li>
			</ul>
		</div>
	</div>
	<!-- end header -->
	<div id="logo">
		<h1><a href="#">ComPrice</a></h1>
		<h2>One for all, all in one</h2>
	</div>

	<!-- 	<div id="banner"></div>
 -->
	<!-- start page -->
	<div id="page">
		<!-- start content -->
		<div id="content">
			<div id="post" class="post"><h2 class="title">Search Products. Compare Prices.</h2>
			<h4>Watch The Magic Happen Here!</h4></div>
		</div>
		<!-- end content -->
		<!-- start sidebar -->
		<div id="sidebar">
			<ul>
				<li id="search">
					<h2>Search</h2>
					<input name="query" type="text" id="query" size="30"/>
					
					Search in: <select id="type" name="type">
						<option value="Mobile">Mobiles</option>
						<option value="Laptop">Laptops</option>
						<option value="Watch">Watches</option>
					</select>
					<br/>
					<input type="radio" class="genspec" name="genspec" value="specific" checked="checked"/>Specific Search<br/>
					<input type="radio" class="genspec" name="genspec" value="general" />General Search<br/>
					<div id="detdiv">
					Price Range:<br/>
					To &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select id="pricefrom" name="pricefrom">
						<option value="-1">Start</option>
						<option value="0">0</option>
						<option value="1000">1000</option>
						<option value="2000">2000</option>
						<option value="3000">3000</option>
						<option value="4000">4000</option>
						<option value="5000">5000</option>
						<option value="10000">10000</option>
						<option value="20000">20000</option>
						<option value="30000">30000</option>
					</select><br/>
					From &nbsp;<select id="priceto" name="priceto">
						<option value="-1">End</option>
						<option value="2000">2000</option>
						<option value="3000">3000</option>
						<option value="4000">4000</option>
						<option value="5000">5000</option>
						<option value="10000">10000</option>
						<option value="20000">20000</option>
						<option value="30000">30000</option>
						<option value="3000000">Max</option>
					</select>
					</div>
					<button type="submit" id="hitme">Comprice!</button>

				</li>
			</ul>
		</div>
		<!-- end sidebar -->
		<div style="clear: both;">&nbsp;</div>
	</div>
	<!-- end page -->
</body>
</html>