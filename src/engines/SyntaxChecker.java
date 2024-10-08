package engines;

import models.OntoBahasa;

/**
 * Untuk melakukan semua proses pengecekan grammar 
 */

public class SyntaxChecker {
	//TODO 
	private static boolean isFN(OntoBahasa x) {
		return x.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				|| x.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA)
				|| x.getKategori().equals(OntoBahasa.KategoriSintaks.PRONOMINA_INTEROGATIV)
				|| x.getKategori().equals(OntoBahasa.KategoriSintaks.NUMERALIA);
	}

	private static boolean isFV(OntoBahasa x) {
		return x.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_VERBAL)
				|| x.getKategori().equals(OntoBahasa.KategoriSintaks.VERBA);
	}
	
	private static boolean isFPrep(OntoBahasa x) {
		return x.getKategori().equals(OntoBahasa.KategoriSintaks.PREPOSISI)
				|| x.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_PREPOSISIONAL);
	}
	
	//untuk mengecek 2 token
	public static boolean isValidSyntax(OntoBahasa b1, OntoBahasa b2) {
		if (b1 == null) {
			return false;
		}
		if (b2 == null) {
			return false;
		}
		if (b1.getKategori() == null || b2.getKategori() == null) {
			return true;
		}
		if (isFN(b1)) {
			return checkLeftFN(b1, b2);
		} else if (isFV(b1)){
			return checkLeftFV(b1, b2);
		} else if (isFPrep(b1)) {
			return checkLeftFPrep(b1, b2);
		} else if (b1.getKategori().equals(OntoBahasa.KategoriSintaks.KLAUSA)){
			return checkLeftKlausa(b1,b2);
		} else if (b1.getKategori().equals(OntoBahasa.KategoriSintaks.KALIMAT)){
			return checkLeftKalimat(b1,b2);
		} else if (b1.getKategori().equals(OntoBahasa.KategoriSintaks.ARTIKULA)){
			return checkLeftArt(b1, b2);
		}
		return false;
	}

	// untuk melakukan pengecekan kiri Kalimat
	private static boolean checkLeftKalimat(OntoBahasa b1, OntoBahasa b2) {
		boolean accepted = false;
		
//		(21)
//		Ki0 ->  Ki1, FN
//		<Ki0 head> = <FN head> 
//		<Ki0 strip> = <FN strip>

		if (isFN(b2)) {
			accepted = (
				// <Ki1 strip kat> = <FN kat>
				b1.getKatS() != null && b1.getKatS().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <Ki1 strip tipe> = = <FN tipe>
				&& b1.getTipeS() != null && b1.getTipeS().contains(b2.getTipeSemantik())
				
				// <FN katArg0> = N 
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
				// <FN Arg1> = 0
				&& b2.getKat1() == null
				// <FN strip> = 0
				&& b2.getKatS() == null
			);
			System.out.println("check Rule (21) Ki FN jadi Ki");
		}
		
//		K0 -> K1 K’(22) OK 10/2/15

		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.KLAUSA)) {
			accepted = (
				// <Ki1 strip kat> = FN
				//b1.getKatS() != null && b1.getKatS().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <Ki1 strip tipe> = <K’ tipe>
				//&& 
				b1.getTipeS() != null && b1.getTipeS().contains(b2.getTipeSemantik())
				
				// <K' Arg1> = 0
				&& b2.getKat1() == null
				// <K' strip> = 0
				&& b2.getKatS() == null
			);
			System.out.println("check Rule (22), K0 -> K1 K'");
		}
		return accepted;
	}
	
	// untuk pengecekan jika kiri Klausa
	private static boolean checkLeftKlausa(OntoBahasa b1, OntoBahasa b2) {
		boolean accepted = false;
		// Ki ->  K’ FN (2)
		if (isFN(b2)) {
			accepted = (
				// <K’ Arg1> = 0
				b1.getKat1() == null
				// <K' strip> = 0
				&& b1.getKatS() == null
				
				// <FN katArg0> = FN 
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				//<FN tipeArg0> = <K’ tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <FN1 Arg1> = 0
				&& b2.getKat1() == null
				//<FN strip> = 0
				&& b2.getKatS() == null
			);
			System.out.println("check Rule (2) K' FN jadi Ki");
		}
		
//		(6)
//		Ki ->  K’ FPrep
//			<Ki head> = <K’ head>
//			<Ki strip> = <K’strip>
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_PREPOSISIONAL)) {
			accepted = (
				// <K’ Arg1> = 0
				b1.getKat1() == null
				// <K’ strip> = 0 
				&& b1.getKatS() == null
				
				// <FPrep katArg0> = FN 
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <FPrep tipeArg0> = <K’tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <FPrep strip> = 0
				&& b2.getKatS() == null
			);
			System.out.println("check Rule (6) K' FPrep jadi Ki");
		}
		
//		(10)
//		Ki -> K’ FV
//			<Ki head> = <K’ head>
//		    	<Ki strip> = <K’ strip>
	
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_VERBAL)) {
			accepted = (
				// <K’ Arg1> = 0
				b1.getKat1() == null
				// <K’ strip> = 0 
				&& b1.getKatS() == null
				
				// 	<FV katArg0> = FN
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <FV tipeArg0> = <K’ tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <FV strip> = 0
				&& b2.getKatS() == null
			);
			System.out.println("check Rule (10) K' FV jadi Ki");
		}
		
//		(18)
//		Ki0 -> K’, Ki1
//			<Ki0 head>  = <K’ head> 
//			<Ki0 strip>  = <K’ strip>
		
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.KALIMAT)) {
			accepted = (
				// <K’ Arg1> = 0
				b1.getKat1() == null
				// <K’ strip> = 0 
				&& b1.getKatS() == null
				
				// <Ki1 strip kat> = FN
				&& b2.getKatS() != null && b2.getKatS().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <Ki1 strip tipe> = <K’ tipe>
				&& b2.getTipeS() != null && b2.getTipeS().contains(b1.getTipeSemantik())
			);
			System.out.println("check Rule (18) K', Ki jadi Ki");
		}
		
//		K0 ->  K’ K1 (20), OK 10/2/15
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.KALIMAT)) {
			accepted = (
				// <K’ Arg1> = 0
				b1.getKat1() == null
				// <K’ strip> = 0 
				&& b1.getKatS() == null
				
				// <Ki1 strip kat> = FN
				//&& b2.getKatS() != null && b2.getKatS().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <Ki1 strip tipe> = <K’ tipe>
				&& b2.getTipeS() != null && b2.getTipeS().contains(b1.getTipeSemantik())
			);
			System.out.println("check Rule (20), K0 -> K' K1");
		}
		
		return accepted;
	}
	
	//jika kiri Prep/Fprep
	private static boolean checkLeftFPrep(OntoBahasa b1, OntoBahasa b2) {
		boolean accepted = false;
		

//		 FPrep -> Prep FN (39) OK (6/2/15)
//			<FPrep Arg0> = <Prep Arg0>
//			<FPrep Arg1> = 0
//		    	<FPrep Arg2> = <Prep Arg2>
//		    	<FPrep head> = <Prep head>
//		    	<FPrep strip> = <FN strip>

		if ((b2.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL) 
				|| b2.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA))) {
			accepted = (
					// <Prep katArg1> = <FN kat>
					b1.getKat1() != null && b1.getKat1().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
					// <Prep tipeArg1> = <FN tipe>
					&& b1.getTipe1() != null && b1.getTipe1().contains(b2.getTipeSemantik())
					
					// <FN katArg0> = N
					&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
					// <FN Arg1> = 0
					&& b2.getKat1() == null
			);
			System.out.println("check Rule (39), Fprep -> Prep FN");
		}
		
//		(8)
//		Ki -> FPrep K’
//			<Ki head> = <K’ head>
//		    <Ki strip> = <K’ strip>
		
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.KLAUSA)) {
			accepted = (
					// <FPrep katArg0> = <FN kat>
					b1.getKat0() != null && b1.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
					// <FPrep tipeArg0> = <FN tipe>
					&& b1.getTipe0() != null && b1.getTipe0().contains(b2.getTipeSemantik())
					// <FPrep strip> = 0
					&& b1.getKatS() == null
					
					// <K' katArg0> = 0 
					&& b2.getKat0() == null
					// <K' strip> = 0
					&& b2.getKatS() == null
			);
			System.out.println("check Rule (8) Fprep K' jadi Ki");
		}
//		(7)
//		Ki -> FPrep FN
//			<Ki head> = <FN head>
//		    	<Ki strip> = <FN strip>
//		
//		if (!accepted && isFN(b2)) {
//			accepted = (
//				// <FPrep katArg0> = <FN kat>
//				b1.getKat0() != null && b1.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
//				// <FPrep tipeArg0> = <FN tipe>
//				&& b1.getTipe0() != null && b1.getTipe0().contains(b2.getTipeSemantik())
//				// <FPrep strip> = 0
//				&& b1.getKatS() == null
//				
//				// <FN katArg0> = N 
//				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
//				// <FN1 Arg1> = 0
//				&& b2.getKat1() == null
//			);
//			System.out.println("check Rule (7) Fprep FN jadi Ki");
//		}
			
		return accepted;
	}
	
	// untuk pengecekan jika kiri Art
	private static boolean checkLeftArt(OntoBahasa b1, OntoBahasa b2) {
		boolean accepted = false;
//		(40)
//		K’ -> Art FN
//		    	<K’ Arg0> = <Art Arg0>
//		    	<K’ Arg1> = 0
//		    	<K’ Arg2> = <Art Arg2>
//		    	<K’ head> = <Art head> 
//		    	<K’ strip> = <FN strip> 
		
		if (isFN(b2)) {
			accepted = (
				// <FN katArg0> = FN
				b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <FN tipeArg0> = <Art tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <FN Arg1> = 0
				&& b2.getKat1() == null
				// <FN strip> = 0
				&& b2.getKatS() == null
			);
			System.out.println("check Rule (40) Art FN jadi K'");
		}
		
		//K' -> Art FV (41) OK (6/2/15)
		if (!accepted && isFV(b2)) {
			accepted = (
				// <FV katArg0> = FN
				b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <FV tipeArg0> = <Art tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <FV strip> = 0
				&& b2.getKatS() == null
			);
			System.out.println("check Rule (41) K' -> Art FV");
		}
		
//		(42)
//		K’ -> Art FPrep
//		    	<K’ Arg0> = <Art Arg0> 
//		    	<K’ Arg1> = 0
//		    	<K’ Arg2> = <Art Arg2>
//		    	<K’ head> = <Art head>
//		    	<K’ strip> = <FPrep strip>

		if (!accepted && isFPrep(b2)) {
			accepted = (
				// <FPrep katArg0> = FN
				b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <FPrep tipeArg0> = <Art tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <FPrep strip> = 0
				&& b2.getKatS() == null
			);
			System.out.println("check Rule (42) Art FPrep jadi K'");
		}
		return accepted;
	}

	// untuk pengecekan jika kiri N/FN
	private static boolean checkLeftFN(OntoBahasa b1, OntoBahasa b2) {
		boolean accepted = false;
		// K -> FN0 FN1 (1) OK (6/2/15)
		if (isFN(b2)) {
			accepted = (
				// <FN0 katArg0> = N
				b1.getKat0() != null && b1.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
				// <FN0 Arg1> = 0
				//&& b1.getKat1() == null
					
				// <FN1 katArg0> = <FN0 kat>
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <FN1 tipeArg0> = <FN0 tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <FN1 Arg1> = 0
				&& b2.getKat1() == null
				// <FN1 strip> = 0
				&& b2.getKatS() == null);
			System.out.println("check Rule (1), K -> FN0 FN1");
		}

		// K -> FN0 FN1 (3) untuk sementara OK (7/2/15)
		if (!accepted && isFN(b2)) {
			accepted = (
				// <FN0 katArg0> = <FN1 kat>
				b1.getKat0() != null && b1.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <FN0 tipeArg0> = <FN1 tipe>
				&& b1.getTipe0() != null && b1.getTipe0().contains(b2.getTipeSemantik())
				// <FN0 Arg1> = 0	
				&& b1.getKat1() == null
				// <FN0 strip> = 0
				&& b1.getKatS() == null
				// <FN1 katArg0> = N
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
				// <FN1 Arg1> = 0
				&& b2.getKat1() == null);
			System.out.println("check Rule (3) K -> FN0 FN1");
		}
		
		// Ki -> FN K' - (4)
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.KLAUSA)) {
			accepted = (
				// <FN0 katArg0> = FN
				b1.getKat0() != null && b1.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <FN tipeArg0> = <K’tipe>
				&& b1.getTipe0() != null && b1.getTipe0().contains(b2.getTipeSemantik())
				// <FN Arg1> = 0
				//&& b1.getKat1() == null
				// <FN strip> = 0
				//&& b1.getKatS() == null
				
				// <K’ Arg1> = 0
				&& b2.getKat1() == null
				// <K’ strip> = 0
				&& b2.getKatS() == null);
			System.out.println("check Rule (4) FN K' hasil = Ki");
		}

		// Ki -> FN FPrep - (5)
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_PREPOSISIONAL)) {
				// System.out.println(b1.getNilai() + " kat0 = " + b1.getKat0());
			accepted = (
				// <FN katArg0> = N
				b1.getKat0() != null && b1.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
				// <FN Arg1> = 0
				&& b1.getKat1() == null

				// <FPrep katArg0> = <FN kat>
				&& b2.getKat0() != null && b2.getKat0().contains(b1.getKategori())
				// <FPrep tipeArg0> = <FN tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <FPrep strip> = 0
				&& b2.getKatS() == null);
			System.out.println("check Rule (5) FN FPrep hasil = Ki");
		}

		// K -> FN FV (9) status OK 6/2/15
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_VERBAL)) {
			accepted = (
				// <FN katArg0> = N
				b1.getKat0() != null && b1.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
				// <FN Arg1> = 0
				&& b1.getKat1() == null

				// <FV katArg0> = <FN kat>
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <FV tipeArg0> = <FN tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <FV strip> = 0
				&& b2.getKatS() == null);
			System.out.println("check Rule (9), K -> FN FV");
		}
				
		// Ki -> FN, Ki1 (17)
//		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.KALIMAT)) {
//			accepted = (
//				// <FN katArg0> = N
//				b1.getKat0() != null && b1.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
//				// <FN Arg1> = 0
//				&& b1.getKat1() == null
//				// <FN strip> = 0
//				&& b1.getKatS() == null
//				// <Ki1 strip kat> = <FN kat>
//				&& b2.getKatS() != null && b2.getKatS().contains(b1.getKategori())
//				// <Ki1 strip tipe> = <FN tipe>
//				&& b2.getTipeS() != null && b2.getTipeS().contains(b1.getTipeSemantik()));
//			System.out.println("check Rule (17) FN, Ki = Ki");
//		}
		
		// K0 -> FN K1 (19) status OK 6/2/15
//		<Ki0 strip> = <FN strip>
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.KALIMAT)) {
			accepted = (
				// <FN katArg0> = N
				b1.getKat0() != null && b1.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
				// <FN Arg1> = 0
				//&& b1.getKat1() == null
				// <FN strip> = 0
				//&& b1.getKatS() == null
				
				// <Ki1 strip kat> = <FN kat>
				//&& b2.getKatS() == null //&& b2.getKatS().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <Ki1 strip tipe> = <FN tipe>
				&& b2.getTipeS() != null && b2.getTipeS().contains(b1.getTipeSemantik()));
			System.out.println("check Rule (19), K -> FN K");
		}
		
		// FN0 -> N FN1 - (26, 27, 28) OK (6/2/15)
		if (!accepted && isFN(b2)) {
			accepted = (
				// <N katArg1> = <FN1 kat>
				b1.getKat1() != null && b1.getKat1().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <N tipeArg1> = <FN1 tipe>
				&& b1.getTipe1() != null && b1.getTipe1().contains(b2.getTipeSemantik())
				// <N strip> = 0
				//&& b1.getKatS() == null
						
				// <FN1 katArg0> = <N kat>
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
				// <FN1 tipeArg0> = <N tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <FN1 Arg1> = 0
				//&& b2.getKat1() == null
				);
			System.out.println("check Rule (26, 27, 28) FN0 -> N FN1");
		}
				
		//FN -> FN Fprep (29)
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_PREPOSISIONAL)) {
			accepted = (
				// <FN1 katArg1> = <FPrep kat>
				b1.getKat1() != null && b1.getKat1().contains(OntoBahasa.KategoriSintaks.FRASA_PREPOSISIONAL)
				// <FN1 empty> = no

				// <FPrep katArg0> = <FN1 kat>
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <FPrep tipeArg0> = <FN1 tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <FPrep strip> = 0
				&& b2.getKatS() == null);
			System.out.println("check Rule (29) FN FPrep = FN");
		}
		
		//FN -> FN Fprep (30)
//		FN0 -> FN1 FPrep
//		<FN0 Arg0> = <FN1 Arg0>
//		<FN0 Arg1> = K’
//		<FN0 Arg2> = <FN1 Arg2>
//		<FN0 head> = <FN1 head> 
//		<FN0 strip> = <FN1 strip>
//		
//		<FN1 katArg1> = <FPrep kat>
//		<FN1 empty> = no
//
//	    	<FPrep katArg0> = <FN1 kat> 
//	    	<FPrep tipeArg0> = <FN1 tipe>
//	    	<FPrep strip> = 0

		
		// FN0 -> FN1 K' (31) OK (6/2/15)
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.KLAUSA)) {
			accepted = (
				// <FN1 katArg1> = <K’ kat>
				b1.getKat1() != null && b1.getKat1().contains(OntoBahasa.KategoriSintaks.KLAUSA)
				
				// <K’ katArg0> = <FN1 kat>
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <K’ tipeArg0> = <FN1 tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <K’ strip> = 0
				&& b2.getKatS() == null);
			System.out.println("check Rule (31), FN0 -> FN1 K'");
		}

		// FV -> FN V (34)
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.VERBA)) {
			accepted = (
				// <FN katArg0> = N
				b1.getKat0() != null && b1.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
				// <FN Arg1> = 0
				&& b1.getKat1() == null
				// <V Arg1> = 0
				&& b2.getKat1() == null
				// <FN strip> = 0
				&& b1.getKatS() == null &&
				// plain V
				
				// <V strip kat> = <FN kat>
				b2.getKatS() != null && b2.getKatS().contains(b1.getKategori())
				// <V strip tipe> = <FN tipe>
				&& b2.getTipeS() != null && b2.getTipeS().contains(b1.getTipeSemantik()));
			System.out.println("check Rule (34) FN V = FV");
		}
		return accepted;
	}
	
	// rule untuk pengecekan jika kiri V/FV
	private static boolean checkLeftFV(OntoBahasa b1, OntoBahasa b2) {
		boolean accepted = false;
		//Ki -> FV FN (11) v
		if (isFN(b2)) {
			accepted = (
				// <V katArg1> = <FN kat>
				b1.getKat0() != null && b1.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <V tipeArg1> = <FN tipe>
				&& b1.getTipe0()!= null && b1.getTipe0().contains(b2.getTipeSemantik())
				// <V strip> = 0
				&& b1.getKatS() == null
				
				//<FN katArg0> = N
				&& b2.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
				//<FN Arg1> = 0
				&& b2.getKat1() == null
			);
			System.out.println("cek rule (11) FV FN = K");
		}
		
		//FV -> V FN (33) status OK 6/2/15
				if (!accepted && isFN(b2)) {
					accepted = (
						// <V katArg1> = <FN kat>
						b1.getKat1() != null && b1.getKat1().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
						// <V tipeArg1> = <FN tipe>
						&& b1.getTipe1()!= null && b1.getTipe1().contains(b2.getTipeSemantik())
						// <V Arg2>  = 0
						&& b1.getKat2() == null
						// <V strip> = 0
						&& b1.getKatS() == null
						
						//<FN katArg0> = N
						&& b2.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
						//<FN Arg1> = 0
						//&& b2.getKat1() == null
						// <FN strip> = 0
						//&& b2.getKatS() == null
					);
					System.out.println("Cek rule (33), FV -> V FN");			
				}
		
//		(12)
//		Ki -> FV K’
//			<Ki head> = <K’ head>
//		    	<Ki strip> = <K’ strip>
		
		if (!accepted && (b2.getKategori().equals(OntoBahasa.KategoriSintaks.KLAUSA))) {
			accepted = (
				// <FV katArg0> = FN 
				b1.getKat0() != null && b1.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
				// <FV tipeArg0> = <K’ tipe>
				&& b1.getTipe0()!= null && b1.getTipe0().contains(b2.getTipeSemantik())
				// <FV strip> = 0
				&& b1.getKatS() == null
				
				//<K’ Arg1> = 0 
				&& b2.getKat1() == null
				// <K’ strip> = 0
				&& b2.getKatS() == null
			);
			System.out.println("Cek rule (12) FV K' = K");			
		}
		
//		(35)
//		FV0 -> FV1 FN
//			<FV0 Arg0> = <FV1 Arg0> 
//			<FV0 Arg1> = <FV1 Arg1>
//			<FV0 Arg2> = <FV1 Arg2>
//			<FV0 head> = <FV1 head>
//			<FV0 strip> = <FV1 strip> 

		if (!accepted && isFV(b2)) {
			accepted = (
				// <FV strip> = 0
				b1.getKatS() == null
				
				//<FN katArg0> = <FV1 kat>
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_VERBAL)
				//<FN tipeArg0> = <FV1 tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipe0())
				//<FN Arg1> = 0
				&& b2.getKat1() == null
				// <FN strip> = 0
				&& b2.getKatS() == null
			);
			System.out.println("Cek rule (35) FV FN = FV");			
		}
		
//		(36)
//		FV0 -> FV1 FN
//			<FV0 Arg0> = <FV1 Arg0>
//			<FV0 Arg1> = <FV1 Arg1>
//			<FV0 Arg2> = <FV1 Arg2>
//			<FV0 head> = <FV1 head> 
//			<FV0 strip> = <FV1 strip>


		if (!accepted && isFV(b2)) {
			accepted = (
				// <FV strip> = 0
				b1.getKatS() == null
				
				//<FN katArg0> = <FV1 kat>
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_VERBAL)
				//<FN tipeArg0> = <FV1 tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipe0())
				//<FN Arg1> = 0
				&& b2.getKat1() == null
				// <FN strip> = 0
				&& b2.getKatS() == null
			);
			System.out.println("Cek rule (36) FV FN = FV");			
		}

//		FV0 -> FV1 FPrep (37) OK (6/2/15)
		if (!accepted && b2.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_PREPOSISIONAL)) {
			accepted = (
				// <FV strip> = 0
				b1.getKatS() == null
				
				//<FPrep katArg0> = <FV1 kat>
				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_VERBAL)
				//<FPrep tipeArg0> = <FV1 tipe>
				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
				// <FN strip> = 0
				&& b2.getKatS() == null
			);
			System.out.println("Cek rule (37) FV0 -> FV1 FPrep");			
		}

////		(38)  
////		FV0 -> FV1 FPrep
////			<FV0 Arg0> = <FV1 Arg0>
////			<FV0 Arg1> = <FV1 Arg1> 
////			<FV0 Arg2> = <FV1 Arg2> 
////			<FV0 head> = <FV1 head>
////			<FV0 strip> = <FV1 strip>
//		
//		if (!accepted && isFPrep(b2)) {
//			accepted = (
//				// <FV strip> = 0
//				b1.getKatS() == null
//				
//				//<FPrep katArg0> = <FV1 kat>
//				&& b2.getKat0() != null && b2.getKat0().contains(OntoBahasa.KategoriSintaks.FRASA_VERBAL)
//				//<FPrep tipeArg0> = <FV1 tipe>
//				&& b2.getTipe0() != null && b2.getTipe0().contains(b1.getTipeSemantik())
//				// <FN strip> = 0
//				&& b2.getKatS() == null
//			);
//			System.out.println("Cek rule (38) FV FPrep = FV");			
//		}
		
		return accepted;
	}
	
	// GABUNG 2 TOKEN
	public static OntoBahasa generateAccepted(OntoBahasa a, OntoBahasa b) {
		System.out.println("TRUE\n" + a + b);
		OntoBahasa hasil = new OntoBahasa(a.getReader());
		if (a.getKategori() == null) {
			if (isFN(b)) {
				hasil.setNilai(a.getNilai() + " " + b.getNilai());
				hasil.addVars(a.getVars());
				hasil.addVars(b.getVars());

				if (a.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(a);
					hasil.addVars(queryLogic);
				}

				if (b.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(b);
					hasil.addVars(queryLogic);
				}
			}
		} else if (b.getKategori() == null) {
			if (isFN(a)) {
				String pertama = a.getNilai();
				String kedua = b.getNilai();

				if (a.getShadowNilai() != null) {
					pertama = a.getShadowNilai();
				}

				if (b.getShadowNilai() != null) {
					kedua = b.getShadowNilai();
				}

				hasil.setNilai(pertama + " " + kedua);
				// Jadi frasa nominal
				hasil.setKategori(OntoBahasa.KategoriSintaks.FRASA_NOMINAL);
				hasil.setTipeSemantik(a.getTipeSemantik());
				hasil.addVars(a.getVars());
				hasil.addVars(b.getVars());

				if (a.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(a);
					hasil.addVars(queryLogic);
				}

				if (b.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(b);
					hasil.addVars(queryLogic);
				}

				// <FN0 Arg0> = <N Arg0>
				hasil.setKat0(a.getKat0());
				hasil.setTipe0(a.getTipe0());
				// <FN0 Arg1> = 0
				hasil.setKat1(null);
				hasil.setTipe1(null);
				// <FN0 Arg2> = <N Arg2>
				hasil.setKat2(a.getKat2());
				hasil.setTipe2(a.getTipe2());
				// <FN0 head> = <N head>
				// <FN0 strip> = <FN1 strip>
				hasil.setKatS(b.getKatS());
				hasil.setTipeS(b.getTipeS());
			}
		} 
		//Ki -> K' FN (2)
		else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.KLAUSA))
				&& (b.getKategori().equals(OntoBahasa.KategoriSintaks.PRONOMINA_INTEROGATIV))) {
			String pertama = a.getNilai();
			String kedua = b.getNilai();

			if (a.getShadowNilai() != null) {
				pertama = a.getShadowNilai();
			}

			if (b.getShadowNilai() != null) {
				kedua = b.getShadowNilai();
			}

			hasil.setNilai(pertama + " " + kedua);
			hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
			hasil.setTipeSemantik(a.getTipeSemantik());
			hasil.addVars(a.getVars());
			hasil.addVars(b.getVars());

			if (a.getNilaiSemantik() != null) {
				String queryLogic = a.getReader().getQueryLogic(a);
				hasil.addVars(queryLogic);
			}

			if (b.getNilaiSemantik() != null) {
				String queryLogic = a.getReader().getQueryLogic(b);
				hasil.addVars(queryLogic);
			}
//			// 
//			hasil.setKat0(a.getKat0());
//			hasil.setTipe0(a.getTipe0());
//			// 
//			hasil.setKat1(null);
//			hasil.setTipe1(null);
//			// 
//			hasil.setKat2(a.getKat2());
//			hasil.setTipe2(a.getTipe2());
			// 
			// <Ki strip> = <FN strip>
			hasil.setKatS(a.getKatS());
			hasil.setTipeS(a.getTipeS());
		}
//		//K -> FN FN (3) catatan : gabung masih manual
//		else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.PRONOMINA_INTEROGATIV) 
//					|| a.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)) 
//				&& (b.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA)
//					|| b.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL))) {
//			String pertama = a.getNilai();
//			String kedua = b.getNilai();
//
//			if (a.getShadowNilai() != null) {
//				pertama = a.getShadowNilai();
//			}
//
//			if (b.getShadowNilai() != null) {
//				kedua = b.getShadowNilai();
//			}
//
//			hasil.setNilai(pertama + " " + kedua);
//			hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
//			hasil.setTipeSemantik(a.getTipeSemantik());
//			hasil.addVars(a.getVars());
//			hasil.addVars(b.getVars());
//
//			if (a.getNilaiSemantik() != null) {
//				String queryLogic = a.getReader().getQueryLogic(a);
//				hasil.addVars(queryLogic);
//			}
//
//			if (b.getNilaiSemantik() != null) {
//				String queryLogic = a.getReader().getQueryLogic(b);
//				hasil.addVars(queryLogic);
//			}
//
////					hasil.setKat0(a.getKat0());
////					hasil.setTipe0(a.getTipe0());
////					// 
////					hasil.setKat1(null);
////					hasil.setTipe1(null);
////					// 
////					hasil.setKat2(a.getKat2());
////					hasil.setTipe2(a.getTipe2());
//					// 
//					// <Ki strip> = <FN strip>
//					hasil.setKatS(a.getKatS());
//					hasil.setTipeS(a.getTipeS());
//				}
		
		// K -> FN0 FN1 (3), EVALUASI (6/2/15)
				else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.PRONOMINA_INTEROGATIV))
						&& (b.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL) || 
								b.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA))) {
					String pertama = a.getNilai();
					String kedua = b.getNilai();

					if (a.getShadowNilai() != null) {
						pertama = a.getShadowNilai();
					}
					if (b.getShadowNilai() != null) {
						kedua = b.getShadowNilai();
					}
					hasil.setNilai(pertama + " " + kedua);
					hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
					hasil.setTipeSemantik(a.getTipeSemantik());
					hasil.addVars(a.getVars());
					hasil.addVars(b.getVars());

					if (a.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(a);
						hasil.addVars(queryLogic);
					}

					if (b.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(b);
						hasil.addVars(queryLogic);
					}
					//<Ki strip> = <FN1 strip>
					hasil.setKatS(b.getKatS());
					hasil.setTipeS(b.getTipeS());
				}
		
		// K -> FN0 FN1 (1), EVALUASI (7/2/15)
				else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL) || 
						a.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA) )
						&& (b.getKategori().equals(OntoBahasa.KategoriSintaks.PRONOMINA_INTEROGATIV))) {
					String pertama = a.getNilai();
					String kedua = b.getNilai();

					if (a.getShadowNilai() != null) {
						pertama = a.getShadowNilai();
					}
					if (b.getShadowNilai() != null) {
						kedua = b.getShadowNilai();
					}
					hasil.setNilai(pertama + " " + kedua);
					hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
					hasil.setTipeSemantik(a.getTipeSemantik());
					hasil.addVars(a.getVars());
					hasil.addVars(b.getVars());

					if (a.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(a);
						hasil.addVars(queryLogic);
					}

					if (b.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(b);
						hasil.addVars(queryLogic);
					}
					//<Ki strip> = <FN0 strip>
					hasil.setKatS(a.getKatS());
					hasil.setTipeS(a.getTipeS());
				}
		
		//K -> FN K' (4) OK 9/2/15
		else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.PRONOMINA_INTEROGATIV) || 
				a.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL))
				&& (b.getKategori().equals(OntoBahasa.KategoriSintaks.KLAUSA))) {
			String pertama = a.getNilai();
			String kedua = b.getNilai();

			if (a.getShadowNilai() != null) {
				pertama = a.getShadowNilai();
			}

			if (b.getShadowNilai() != null) {
				kedua = b.getShadowNilai();
			}

			hasil.setNilai(pertama + " " + kedua);
			hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
			hasil.setTipeSemantik(a.getTipeSemantik());
			hasil.addVars(a.getVars());
			hasil.addVars(b.getVars());

			if (a.getNilaiSemantik() != null) {
				String queryLogic = a.getReader().getQueryLogic(a);
				hasil.addVars(queryLogic);
			}

			if (b.getNilaiSemantik() != null) {
				String queryLogic = a.getReader().getQueryLogic(b);
				hasil.addVars(queryLogic);
			}
//			// 
//			hasil.setKat0(a.getKat0());
//			hasil.setTipe0(a.getTipe0());
//			// 
//			hasil.setKat1(null);
//			hasil.setTipe1(null);
//			// 
//			hasil.setKat2(a.getKat2());
//			hasil.setTipe2(a.getTipe2());
			// 
			// <Ki strip> = <FN strip>
			hasil.setKatS(a.getKatS());
			hasil.setTipeS(a.getTipeS());
		}
		
		//FV -> FV FN (11)
		else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_VERBAL))
			&& ((b.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)) || 
					(b.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA)))) {
			String pertama = a.getNilai();
			String kedua = b.getNilai();
			if (a.getShadowNilai() != null) {
				pertama = a.getShadowNilai();
			}
			if (b.getShadowNilai() != null) {
				kedua = b.getShadowNilai();
			}
			hasil.setNilai(pertama + " " + kedua);
			hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
			hasil.setTipeSemantik(a.getTipeSemantik());
			hasil.addVars(a.getVars());
			hasil.addVars(b.getVars());

			if (a.getNilaiSemantik() != null) {
				String queryLogic = a.getReader().getQueryLogic(a);
				hasil.addVars(queryLogic);
			}

			if (b.getNilaiSemantik() != null) {
				String queryLogic = a.getReader().getQueryLogic(b);
				hasil.addVars(queryLogic);
			}
//					// 
//					hasil.setKat0(a.getKat0());
//					hasil.setTipe0(a.getTipe0());
//					// 
//					hasil.setKat1(null);
//					hasil.setTipe1(null);
//					// 
//					hasil.setKat2(a.getKat2());
//					hasil.setTipe2(a.getTipe2());
//					// 
//					// <FV strip> = <FN strip>
					hasil.setKatS(a.getKatS());
					hasil.setTipeS(a.getTipeS());
		}
		
		// FN0 -> N FN1 (26,27,28) OK (6/2/15)
		else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA)
					|| a.getKategori().equals(OntoBahasa.KategoriSintaks.NUMERALIA)) && (isFN(b))) {
			String pertama = a.getNilai();
			String kedua = b.getNilai();

			if (a.getShadowNilai() != null) {
				pertama = a.getShadowNilai();
			}
			if (b.getShadowNilai() != null) {
				kedua = b.getShadowNilai();
			}
			hasil.setNilai(pertama + " " + kedua);
			hasil.setKategori(OntoBahasa.KategoriSintaks.FRASA_NOMINAL);
			hasil.setTipeSemantik(a.getTipeSemantik());
			hasil.addVars(a.getVars());
			hasil.addVars(b.getVars());

			if (a.getNilaiSemantik() != null) {
				String queryLogic = a.getReader().getQueryLogic(a);
				hasil.addVars(queryLogic);
			}

			if (b.getNilaiSemantik() != null) {
				String queryLogic = a.getReader().getQueryLogic(b);
				hasil.addVars(queryLogic);
			}
			// <FN0 Arg0> = <N Arg0>
			hasil.setKat0(a.getKat0());
			hasil.setTipe0(a.getTipe0());
			// <FN0 Arg1> = 0
			hasil.setKat1(null);
			hasil.setTipe1(null);
			// <FN0 Arg2> = <N Arg2>
			hasil.setKat2(a.getKat2());
			hasil.setTipe2(a.getTipe2());
			// <FN0 head> = <N head>
			// <FN0 strip> = <FN1 strip>
			hasil.setKatS(b.getKatS());
			hasil.setTipeS(b.getTipeS());
		}
		
		//K -> FN FV (9) OK 6/2/15
				else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.PRONOMINA_INTEROGATIV) 
						|| a.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA) || 
						a.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL))
						&& (b.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_VERBAL))) {
					System.out.println("test");
					String pertama = a.getNilai();
					String kedua = b.getNilai();

					if (a.getShadowNilai() != null) {
						pertama = a.getShadowNilai();
					}
					if (b.getShadowNilai() != null) {
						kedua = b.getShadowNilai();
					}
					hasil.setNilai(pertama + " " + kedua);
					hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
					hasil.setTipeSemantik(a.getTipeSemantik());
					hasil.addVars(a.getVars());
					hasil.addVars(b.getVars());

					if (a.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(a);
						hasil.addVars(queryLogic);
					}

					if (b.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(b);
						hasil.addVars(queryLogic);
					}
					//<Ki head> = <FN0 head>
					//<Ki strip> = <FN0 strip>
					hasil.setKatS(a.getKatS());
					hasil.setTipeS(a.getTipeS());
				}
		
//		FN -> FN FPrep (29)
		else if (a.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA)
				&& b.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_PREPOSISIONAL)) {
					String pertama = a.getNilai();
					String kedua = b.getNilai();

					if (a.getShadowNilai() != null) {
						pertama = a.getShadowNilai();
					}

					if (b.getShadowNilai() != null) {
						kedua = b.getShadowNilai();
					}

					hasil.setNilai(pertama + " " + kedua);
					// Jadi frasa verba
					hasil.setKategori(OntoBahasa.KategoriSintaks.FRASA_NOMINAL);
					hasil.setTipeSemantik(a.getTipeSemantik());
					hasil.addVars(a.getVars());
					hasil.addVars(b.getVars());

					if (a.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(a);
						hasil.addVars(queryLogic);
					}

					if (b.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(b);
						hasil.addVars(queryLogic);
					}
//					<FV0 Arg0> = <FV1 Arg0> 
					hasil.setKat0(a.getKat0());
					hasil.setTipe0(a.getTipe0());
//					<FV0 Arg1> = <FV1 Arg1>
					hasil.setKat1(null);
					hasil.setTipe1(null);
//					<FV0 Arg2> = <FV1 Arg2>
					hasil.setKat2(a.getKat2());
					hasil.setTipe2(a.getTipe2());
//					<FV0 strip> = <FV1 strip>
					hasil.setKatS(a.getKatS());
					hasil.setTipeS(a.getTipeS());
				}
		
		//FN0 -> FN1 K' (31) OK (6/2/15)
				else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA))
						&& ((b.getKategori().equals(OntoBahasa.KategoriSintaks.KLAUSA)))) {
						String pertama = a.getNilai();
						String kedua = b.getNilai();
						if (a.getShadowNilai() != null) {
							pertama = a.getShadowNilai();
						}
						if (b.getShadowNilai() != null) {
							kedua = b.getShadowNilai();
						}
						hasil.setNilai(pertama + " " + kedua);
						hasil.setKategori(OntoBahasa.KategoriSintaks.FRASA_NOMINAL);
						hasil.setTipeSemantik(a.getTipeSemantik());
						hasil.addVars(a.getVars());
						hasil.addVars(b.getVars());

						if (a.getNilaiSemantik() != null) {
							String queryLogic = a.getReader().getQueryLogic(a);
							hasil.addVars(queryLogic);
						}

						if (b.getNilaiSemantik() != null) {
							String queryLogic = a.getReader().getQueryLogic(b);
							hasil.addVars(queryLogic);
						}
						// <FN0 Arg0> = <FN1 Arg0> 
						hasil.setKat0(a.getKat0());
						hasil.setTipe0(a.getTipe0());
						// <FN0 Arg1> = 0
						hasil.setKat1(null);
						hasil.setTipe1(null);
						// <FN0 Arg2> = <FN1 Arg2>
						hasil.setKat2(a.getKat2());
						hasil.setTipe2(a.getTipe2());
						// <FN0 strip> = <FN1 strip>
						hasil.setKatS(a.getKatS());
						hasil.setTipeS(a.getTipeS());
				}
		
		//FV -> V FN (33) OK 6/2/15
		else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.VERBA))
				&& ((b.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)) || 
						(b.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA)) || 
						(b.getKategori().equals(OntoBahasa.KategoriSintaks.PRONOMINA_INTEROGATIV)))) {
				String pertama = a.getNilai();
				String kedua = b.getNilai();
				if (a.getShadowNilai() != null) {
					pertama = a.getShadowNilai();
				}
				if (b.getShadowNilai() != null) {
					kedua = b.getShadowNilai();
				}
				hasil.setNilai(pertama + " " + kedua);
				hasil.setKategori(OntoBahasa.KategoriSintaks.FRASA_VERBAL);
				hasil.setTipeSemantik(a.getTipeSemantik());
				hasil.addVars(a.getVars());
				hasil.addVars(b.getVars());

				if (a.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(a);
					hasil.addVars(queryLogic);
				}

				if (b.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(b);
					hasil.addVars(queryLogic);
				}
				// <FV Arg0> = <V Arg0>
				hasil.setKat0(a.getKat0());
				hasil.setTipe0(a.getTipe0());
				// <FV Arg1> = 0
				hasil.setKat1(null);
				hasil.setTipe1(null);
				// <FV Arg2> = <V Arg2>
				hasil.setKat2(a.getKat2());
				hasil.setTipe2(a.getTipe2());
				// <FV head> = <V head>
				// <FV strip> = <FN strip>
				hasil.setKatS(b.getKatS());
				hasil.setTipeS(b.getTipeS());
		}

		//K0 -> FN K1 (19) OK 6/2/15
		else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA)||
				a.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)) &&
				((b.getKategori().equals(OntoBahasa.KategoriSintaks.KALIMAT)))) {
				String pertama = a.getNilai();
				String kedua = b.getNilai();
				if (a.getShadowNilai() != null) {
					pertama = a.getShadowNilai();
				}
				if (b.getShadowNilai() != null) {
					kedua = b.getShadowNilai();
				}
				hasil.setNilai(pertama + " " + kedua);
				hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
				hasil.setTipeSemantik(a.getTipeSemantik());
				hasil.addVars(a.getVars());
				hasil.addVars(b.getVars());

				if (a.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(a);
					hasil.addVars(queryLogic);
				}

				if (b.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(b);
					hasil.addVars(queryLogic);
				}
				//<K0 strip> = <FN strip>
				hasil.setKatS(a.getKatS());
				hasil.setTipeS(a.getTipeS());
		}

//		K’ -> Art FN  *(OK 29 may 2015)
		else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.ARTIKULA))
				&& (b.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)||b.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA))) {
					String pertama = a.getNilai();
					String kedua = b.getNilai();

					if (a.getShadowNilai() != null) {
						pertama = a.getShadowNilai();
					}

					if (b.getShadowNilai() != null) {
						kedua = b.getShadowNilai();
					}

					hasil.setNilai(pertama + " " + kedua);
					// Jadi Klausa
					hasil.setKategori(OntoBahasa.KategoriSintaks.KLAUSA);
					hasil.setTipeSemantik(a.getTipeSemantik());
					hasil.addVars(a.getVars());
					hasil.addVars(b.getVars());

					if (a.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(a);
						hasil.addVars(queryLogic);
					}

					if (b.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(b);
						hasil.addVars(queryLogic);
					}
					
					// <K’ Arg0> = <Art Arg0>
					hasil.setKat0(a.getKat0());
					hasil.setTipe0(a.getTipe0());
					// <K’ Arg1> = 0
					hasil.setKat1(null);
					hasil.setTipe1(null);
					// <K’ Arg2> = <Art Arg2>
					hasil.setKat2(a.getKat2());
					hasil.setTipe2(a.getTipe2());
					// <K’ strip> = <FN strip>
					hasil.setKatS(b.getKatS());
					hasil.setTipeS(b.getTipeS());
				}
		
//		K’ -> Art FV (41) OK (6/2/15)
		else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.ARTIKULA))
			&& (b.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_VERBAL)||b.getKategori().equals(OntoBahasa.KategoriSintaks.VERBA))) {
				String pertama = a.getNilai();
				String kedua = b.getNilai();

				if (a.getShadowNilai() != null) {
					pertama = a.getShadowNilai();
				}

				if (b.getShadowNilai() != null) {
					kedua = b.getShadowNilai();
				}

				hasil.setNilai(pertama + " " + kedua);
				// Jadi frasa Klausa
				hasil.setKategori(OntoBahasa.KategoriSintaks.KLAUSA);
				hasil.setTipeSemantik(a.getTipeSemantik());
				hasil.addVars(a.getVars());
				hasil.addVars(b.getVars());

				if (a.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(a);
					hasil.addVars(queryLogic);
				}

				if (b.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(b);
					hasil.addVars(queryLogic);
				}
				
				// <K’ Arg0> = <Art Arg0>
				hasil.setKat0(a.getKat0());
				hasil.setTipe0(a.getTipe0());
				// <K’ Arg1> = 0
				hasil.setKat1(null);
				hasil.setTipe1(null);
				// <K’ Arg2> = <Art Arg2>
				hasil.setKat2(a.getKat2());
				hasil.setTipe2(a.getTipe2());
				// <K’ strip> = <FV strip>
				hasil.setKatS(b.getKatS());
				hasil.setTipeS(b.getTipeS());
			}
		
		// Fprep -> Prep FN (39) OK (6/2/15)
		else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.PREPOSISI))
				&& (b.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_NOMINAL)
						|| (b.getKategori().equals(OntoBahasa.KategoriSintaks.NOMINA)))) {
					String pertama = a.getNilai();
					String kedua = b.getNilai();

					if (a.getShadowNilai() != null) {
						pertama = a.getShadowNilai();
					}

					if (b.getShadowNilai() != null) {
						kedua = b.getShadowNilai();
					}

					hasil.setNilai(pertama + " " + kedua);
					// Jadi frasa verba
					hasil.setKategori(OntoBahasa.KategoriSintaks.FRASA_PREPOSISIONAL);
					hasil.setTipeSemantik(a.getTipeSemantik());
					hasil.addVars(a.getVars());
					hasil.addVars(b.getVars());

					if (a.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(a);
						hasil.addVars(queryLogic);
					}

					if (b.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(b);
						hasil.addVars(queryLogic);
					}
					
					// <FPrep Arg0> = <Prep Arg0>
					hasil.setKat0(a.getKat0());
					hasil.setTipe0(a.getTipe0());
					// <FPrep Arg1> = 0
					hasil.setKat1(null);
					hasil.setTipe1(null);
					// <FPrep Arg2> = <Prep Arg2>
					hasil.setKat2(a.getKat2());
					hasil.setTipe2(a.getTipe2());
					// <FPrep strip> = <FN strip>
					hasil.setKatS(b.getKatS());
					hasil.setTipeS(b.getTipeS());
				}

//		FV0 -> FV1 FPrep (37) OK (6/2/15)
		else if ((isFV(a))
				&& (b.getKategori().equals(OntoBahasa.KategoriSintaks.FRASA_PREPOSISIONAL))) {
					String pertama = a.getNilai();
					String kedua = b.getNilai();

					if (a.getShadowNilai() != null) {
						pertama = a.getShadowNilai();
					}

					if (b.getShadowNilai() != null) {
						kedua = b.getShadowNilai();
					}

					hasil.setNilai(pertama + " " + kedua);
					// Jadi frasa verba
					hasil.setKategori(OntoBahasa.KategoriSintaks.FRASA_VERBAL);
					hasil.setTipeSemantik(a.getTipeSemantik());
					hasil.addVars(a.getVars());
					hasil.addVars(b.getVars());

					if (a.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(a);
						hasil.addVars(queryLogic);
					}

					if (b.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(b);
						hasil.addVars(queryLogic);
					}
//					<FV0 Arg0> = <FV1 Arg0> 
					hasil.setKat0(a.getKat0());
					hasil.setTipe0(a.getTipe0());
//					<FV0 Arg1> = <FV1 Arg1>
					hasil.setKat1(a.getKat1());
					hasil.setTipe1(a.getTipe1());
//					<FV0 Arg2> = <FV1 Arg2>
					hasil.setKat2(a.getKat2());
					hasil.setTipe2(a.getTipe2());
//					<FV0 strip> = <FV1 strip>
					hasil.setKatS(a.getKatS());
					hasil.setTipeS(a.getTipeS());
				}
		
		//K0 -> K1 K' (22) 10/2/15
		else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.KALIMAT))
				&& (b.getKategori().equals(OntoBahasa.KategoriSintaks.KLAUSA))) {
					String pertama = a.getNilai();
					String kedua = b.getNilai();

					if (a.getShadowNilai() != null) {
						pertama = a.getShadowNilai();
					}

					if (b.getShadowNilai() != null) {
						kedua = b.getShadowNilai();
					}

					hasil.setNilai(pertama + " " + kedua);
					// Jadi frasa verba
					hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
					hasil.setTipeSemantik(a.getTipeSemantik());
					hasil.addVars(a.getVars());
					hasil.addVars(b.getVars());

					if (a.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(a);
						hasil.addVars(queryLogic);
					}

					if (b.getNilaiSemantik() != null) {
						String queryLogic = a.getReader().getQueryLogic(b);
						hasil.addVars(queryLogic);
					}
					hasil.setKatS(b.getKatS());
					hasil.setTipeS(b.getTipeS());
				}
		//K0 -> K' K1 (20) OK 10/2/15
				else if ((a.getKategori().equals(OntoBahasa.KategoriSintaks.KLAUSA))
						&& (b.getKategori().equals(OntoBahasa.KategoriSintaks.KALIMAT))) {
							String pertama = a.getNilai();
							String kedua = b.getNilai();

							if (a.getShadowNilai() != null) {
								pertama = a.getShadowNilai();
							}

							if (b.getShadowNilai() != null) {
								kedua = b.getShadowNilai();
							}

							hasil.setNilai(pertama + " " + kedua);
							// Jadi frasa verba
							hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
							hasil.setTipeSemantik(a.getTipeSemantik());
							hasil.addVars(a.getVars());
							hasil.addVars(b.getVars());

							if (a.getNilaiSemantik() != null) {
								String queryLogic = a.getReader().getQueryLogic(a);
								hasil.addVars(queryLogic);
							}

							if (b.getNilaiSemantik() != null) {
								String queryLogic = a.getReader().getQueryLogic(b);
								hasil.addVars(queryLogic);
							}
							hasil.setKatS(a.getKatS());
							hasil.setTipeS(a.getTipeS());
						}
		System.out.println("[" + a.getNilai() + ", " +a.getShadowNilai()+ " ("+ a.getKategori() + ")] + [" 
				+ b.getNilai() + ", " +b.getShadowNilai()+" (" + b.getKategori() + ")] = ["
				+ hasil.getNilai() + " (" + hasil.getKategori() + ")]\n");
//		System.out.println("hasil setelah pengecekan bahasa : "+hasil);
		return hasil;
	}

	// untuk mengecek 3 token 
	public static boolean isValidSyntax(OntoBahasa b1, OntoBahasa b2, OntoBahasa b3) {
			if (b1 == null) {
				return false;
			}
			if (b2 == null) {
				return false;
			}

			if (b3 == null) {
				return false;
			}

			if (b1.getKategori() == null || b2.getKategori() == null
					|| b3.getKategori() == null) {
				return true;
			}

//			if (isFN(b1)) {
//				return checkFN(b1, b2, b3);
//			}
			// System.out.println("================================================");
			return false;
		}
	
	private static boolean checkFN(OntoBahasa b1, OntoBahasa b2, OntoBahasa b3) {
		boolean accepted = false;
		// FN0 FN1 FV
//		if (isFN(b2)
//				&& b3.getKategori().equals(
//						OntoBahasa.KategoriSintaks.FRASA_VERBAL)) {
//			accepted = (
//			// <FN0 katArg0> = <FV kat>
//			b1.getKat0() != null
//					&& b1.getKat0().contains(b3.getKategori())
//					&&
//					// <FN0 tipeArg0> = <FV tipe>
//					b1.getTipe0() != null
//					&& b1.getTipe0().contains(b3.getTipeSemantik())
//					&&
//					// <FN1 katArg0> = N
//					b2.getKat0() != null
//					&& b2.getKat0().contains(OntoBahasa.KategoriSintaks.NOMINA)
//					&&
//					// <FN1 Arg1> = 0
//					b2.getKat1() == null
//					&&
//					// <FN1 empty> = no
//
//					// <FV katArg0> = <FN1 kat>
//					b3.getKat0() != null
//					&& b3.getKat0().contains(b2.getKategori()) &&
//					// <FV tipeArg0> = <FN1 tipe>
//					b3.getTipe0() != null && b3.getTipe0().contains(
//					b2.getTipeSemantik())
//			// <FV strip> = 0
//			);
//		}

		// FN K’ FV
//		if (!accepted
//				&& b2.getKategori().equals(OntoBahasa.KategoriSintaks.KLAUSA)
//				&& b3.getKategori().equals(
//						OntoBahasa.KategoriSintaks.FRASA_VERBAL)) {
//			accepted = (
//			// <FN katArg0> = <FV kat>
//			b1.getKat0() != null
//					&& b1.getKat0().contains(b3.getKategori())
//					&&
//					// <FN tipeArg0> = <FV tipe>
//					b1.getTipe0() != null
//					&& b1.getTipe0().contains(b3.getTipeSemantik())
//					&&
//					// <K' Arg1> = 0
//					b2.getKat1() == null
//					&&
//					// <K' strip> = no
//
//					// <FV katArg0> = FN
//					b3.getKat0() != null
//					&& b3.getKat0().contains(
//							OntoBahasa.KategoriSintaks.FRASA_NOMINAL) &&
//					// <FV tipeArg0> = <K’ tipe>
//					b3.getTipe0() != null && b3.getTipe0().contains(
//					b2.getTipeSemantik())
//			// <FV strip> = 0
//			);
//		}

		return accepted;
	}
	
	//penggabungan 3 token
	public static OntoBahasa generateAccepted(OntoBahasa a, OntoBahasa b, OntoBahasa c) {
		System.out.println("bahasa 1 " + a);
		System.out.println("bahasa 2 " + b);
		OntoBahasa hasil = new OntoBahasa(a.getReader());

		if (a.getKategori() == null) {
			if (isFN(b)
					&& c.getKategori().equals(
							OntoBahasa.KategoriSintaks.FRASA_VERBAL)) {
				hasil.setNilai(a.getNilai() + " " + b.getNilai() + " "
						+ c.getNilai());
				hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
				hasil.addVars(a.getVars());
				hasil.addVars(b.getVars());
				hasil.addVars(c.getVars());

				if (a.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(a);
					hasil.addVars(queryLogic);
				}

				if (b.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(b);
					hasil.addVars(queryLogic);
				}

				if (c.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(c);
					hasil.addVars(queryLogic);
				}
			} else if (b.getKategori()
					.equals(OntoBahasa.KategoriSintaks.KLAUSA)
					&& c.getKategori().equals(
							OntoBahasa.KategoriSintaks.FRASA_VERBAL)) {
				hasil.setNilai(a.getNilai() + " " + b.getNilai() + " "
						+ c.getNilai());
				hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
				hasil.addVars(a.getVars());
				hasil.addVars(b.getVars());
				hasil.addVars(c.getVars());

				if (a.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(a);
					hasil.addVars(queryLogic);
				}

				if (b.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(b);
					hasil.addVars(queryLogic);
				}

				if (c.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(c);
					hasil.addVars(queryLogic);
				}
			}
		} else if (b.getKategori() == null) {
			if (isFN(a)
					&& c.getKategori().equals(
							OntoBahasa.KategoriSintaks.FRASA_VERBAL)) {
				String pertama = a.getNilai();
				String kedua = b.getNilai();
				String ketiga = c.getNilai();

				if (a.getShadowNilai() != null) {
					pertama = a.getShadowNilai();
				}

				if (b.getShadowNilai() != null) {
					kedua = b.getShadowNilai();
				}

				if (c.getShadowNilai() != null) {
					ketiga = c.getShadowNilai();
				}

				hasil.setNilai(pertama + " " + kedua + " " + ketiga);
				// Jadi Ki
				hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
				hasil.setTipeSemantik(a.getTipeSemantik());
				hasil.addVars(a.getVars());
				hasil.addVars(b.getVars());
				hasil.addVars(c.getVars());

				if (a.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(a);
					hasil.addVars(queryLogic);
				}

				if (b.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(b);
					hasil.addVars(queryLogic);
				}

				if (c.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(c);
					hasil.addVars(queryLogic);
				}

				hasil.setKatS(b.getKatS());
				hasil.setTipeS(b.getTipeS());
			}
		} else if (isFN(a)) {
			if (isFN(b)) {
				String pertama = a.getNilai();
				String kedua = b.getNilai();
				String ketiga = c.getNilai();

				if (a.getShadowNilai() != null) {
					pertama = a.getShadowNilai();
				}

				if (b.getShadowNilai() != null) {
					kedua = b.getShadowNilai();
				}

				if (c.getShadowNilai() != null) {
					ketiga = c.getShadowNilai();
				}

				hasil.setNilai(pertama + " " + kedua + " " + ketiga);
				// Jadi Ki
				hasil.setKategori(OntoBahasa.KategoriSintaks.KALIMAT);
				hasil.setTipeSemantik(a.getTipeSemantik());
				hasil.addVars(a.getVars());
				hasil.addVars(b.getVars());
				hasil.addVars(c.getVars());

				if (a.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(a);
					hasil.addVars(queryLogic);
				}

				if (b.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(b);
					hasil.addVars(queryLogic);
				}

				if (c.getNilaiSemantik() != null) {
					String queryLogic = a.getReader().getQueryLogic(c);
					hasil.addVars(queryLogic);
				}

				hasil.setKatS(b.getKatS());
				hasil.setTipeS(b.getTipeS());
			}
		}

		return hasil;
	}

}
