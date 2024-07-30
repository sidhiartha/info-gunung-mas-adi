<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GunungBeta: Indonesia Mountaineering Knowledge Engine</title>
<meta name="description" content="knowledge engine for mountainering" />

<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css">
<link rel="stylesheet" type="text/css" href="css/in.css">
<link rel="stylesheet" type="text/css" href="css/footer.css">
<link rel="stylesheet" href="js/lib/jqueryui/themes/smoothness/jquery-ui.css">

<script src="js/lib/jquery/jquery-1.11.1.min.js"></script>
<script src="js/lib/jqueryui/jquery-ui.min-1.11.1.js"></script>
<script src="js/lib/gb.js"></script>

<script>
$(function() {
    if( document.cookie.indexOf( "runOnce" ) < 0 ) {
     $( "#dialog" ).text("website ini masih dalam proses pengujian, informasi gunung yang ada hanya pada gunung-gunung di jawa tengah, informasi jalur pendakian hanya jalur pendakian gunung merapi dan merbabu");
		$( "#dialog" ).dialog({
            modal: true,
            resizable: false,
            show: 'slide',
            buttons: {
                Ok: function() {
                    $( this ).dialog( "close" );
                    document.cookie = "runOnce=true";
                }
            }
        });
    }
});
</script>

</head>

<body>
	<!--<jsp:include page="header.html"></jsp:include> -->
	<div id="dialog" title="Catatan"></div>

	<div class="box_utama">
		<form action="token" id="calculate" method="get" class="index">
			<img id="logo" src="images/headericon.png" />
			<fieldset id="pos_1">
				<label id="masukkan"> informasi <strong>gunung / jalur pendakian gunung</strong> apa yang ingin anda cari : </label>
				<a href="bantuan.jsp">
				<img src="images/help-512.png" style="width:22px; height:22px; border:0px; float:right; margin-right:-6px">
				</a> 
				<input name="i" id="i" maxlength="200" type="text" placeholder="Pencarian..." />
				<input type="submit" title="proses" value="" style="width: 0px; height: 0px; opacity: 0" />
			</fieldset>
		</form>
	</div>

	<jsp:include page="footer.html"></jsp:include>
</body>
</html>
