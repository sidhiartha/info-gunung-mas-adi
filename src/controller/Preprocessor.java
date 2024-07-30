package controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletContext;
import models.OntoBahasa;
import models.OutputType;
import com.hp.hpl.jena.query.ResultSet;
import engines.Stack;
import engines.SyntaxChecker;

public class Preprocessor {
	// : Menentukan jenis pesan output
	public static enum ResponseType {
		GENERAL_OUTPUT, ERROR_INPUT, ERROR_SYNTAX,
	}

	//  : reader ke ontologi bahasa
	private OntoBahasaReader bahasaReader;
	//  : reader ke ontologi gunung
	private MountaineeringReader mountReader;
	//  : menyimpan hasil pengecekan ke ontologi bahasa yang di temukan
	private List<List<OntoBahasa>> bahasas;
	//  : menyimpan hasil pengecekan ke ontologi gunung yang di temukan

	private List<String> listBahasa;
	private List<String> listGunung;
	private List<String> listFoundGunung;
	private List<String> listFoundBahasa;
	private List<String> listKataKetemu;
	private List<String> listKataTidakKetemu;

	public Preprocessor(ServletContext context) {
		long start = System.currentTimeMillis();

		bahasaReader = new OntoBahasaReader(context);
		mountReader = new MountaineeringReader(context);
		bahasas = new ArrayList<List<OntoBahasa>>();

		listBahasa = bahasaReader.getAllKata();
		listGunung = mountReader.getAllKata();

		listKataKetemu = new ArrayList<String>();
		listFoundGunung = new ArrayList<String>();
		listFoundBahasa = new ArrayList<String>();
		listKataTidakKetemu = new ArrayList<String>();

		System.out.println("Jumlah kata dalam ontologi Bahasa : "
				+ listBahasa.size());
		System.out.println("Jumlah kata dalam ontologi Mountaineering : "
				+ listGunung.size());
		long end = System.currentTimeMillis();
		System.out.println("Waktu konstruksi preproses = "
				+ (end - start));
	}

	public boolean CheckIsSentenceValid(List<String> sentence) {
		long start = System.currentTimeMillis();
		boolean result = ValidateInput(sentence, 0);
		if (result) {
			// semntara cukup, prepared for something
		}
		long end = System.currentTimeMillis();
		System.out.println("waktu untuk validasi = "
				+ (end - start));
		return result;
	}

	// cek kata di ontologi bahasa
	boolean CheckIfWordsInBahasa(String word) {
		if (listBahasa == null) {
			return false;
		}
		for (String item : listBahasa) {
			if (item.toLowerCase().equals(word.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	// cek kata di ontologi domain
	boolean CheckIfWordsInGunung(String word) {
		if (listGunung == null) {
			return false;
		}
		for (String item : listGunung) {
			if (item.toLowerCase().equals(word.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	// validasi input
	public boolean ValidateInput(List<String> sentence, int beginCheckIndex) {
		int wordsCount = sentence.size();
		String wordToCheck = "";
		String trueWord = "";

		int indexTrueWord = -1;
		int wordsKategory = -1;

		if (beginCheckIndex > wordsCount - 1) {
			System.out.println("kata tidak ketemu : " + listKataTidakKetemu);
			System.out.println("kata ketemu : " + listKataKetemu);
			System.out.println("kata dari Ontologi Bahasa : " + listFoundBahasa);
			System.out.println("kata diluar Ontologi Bahasa : " + listFoundGunung);
			if (listKataTidakKetemu.size() == 0) {
				if (listFoundGunung.size() == 0 || listFoundGunung.size() > 4) {
					return false;
				}
				return true;
			} else if (listKataTidakKetemu.size() > 0) {
				return false;
			}
			return !(listKataTidakKetemu.size() > 0);
		}

		for (int i = beginCheckIndex; i < sentence.size(); i++) {
			if (i == beginCheckIndex) {
				wordToCheck += sentence.get(i).trim();
				System.out.println("wordtocek : " + wordToCheck);
			} else {
				wordToCheck += " " + sentence.get(i).trim();
				System.out.println("wordtocek : " + wordToCheck);
			}
			if (CheckIfWordsInBahasa(wordToCheck)) {
				trueWord = wordToCheck;
				wordsKategory = 0;
				indexTrueWord = i;
				continue;
			}
			if (CheckIfWordsInGunung(wordToCheck)) {
				trueWord = wordToCheck;
				wordsKategory = 1;
				indexTrueWord = i;
			}
		}

		if (indexTrueWord >= 0) {
			if (wordsKategory == 0) {
				listFoundBahasa.add(trueWord);
				String query = bahasaReader.createSimpleQuery(trueWord);
				ResultSet rs = bahasaReader.runRawQuery(query);
				List<OntoBahasa> bahasas = bahasaReader
						.simpleResultSetToListBahasa(rs);
				this.bahasas.add(0, bahasas);
			} else if (wordsKategory == 1) {
				//System.out.println("trueword : "+trueWord);
				listFoundGunung.add(trueWord);
				List<OntoBahasa> tempBahasa = bahasaReader.queryTemplate(trueWord);
				bahasas.add(0, tempBahasa);
			}
			listKataKetemu.add(trueWord);
			//System.out.println("input setelah preproses : " + bahasas);
			return ValidateInput(sentence, indexTrueWord + 1);
		} else {
			listKataTidakKetemu.add(sentence.get(beginCheckIndex));
			return ValidateInput(sentence, beginCheckIndex + 1);
		}
	}

	// buat pesan ke user
	public String generateResponseMessage(ResponseType type, String kalimat) {
		String pesan = "";
		String info = "<br><br>Mohon periksa kembali apakah input yang anda berikan sudah sesuai dengan petunjuk berikut: "
				+ "<br>- input harus mengandung kata objek dari informasi yang anda inginkan"
				+ "<br>- input dapat berupa kata, frasa, klausa, kalimat tanya dan kalimat perintah yang sesuai dengan tata bahasa indonesia baku"
				+ "<br>- kata penyusun input dapat berupa nomina (kata benda), verba(kata kerja), preposisi, numeralia, artikula (yang), pronomina, adverbia"
				+ "<br>- sistem tidak memproses input berupa kalimat konfirmatif, kalimat perbandingan atau yang melibatkan perhitungan, kalimat majemuk setara dengan konjungtor (dan)"
				+ "";
		switch (type) {
			case ERROR_INPUT:
				if (listKataTidakKetemu.size() > 0) {
					pesan += "Maaf, Sistem tidak dapat memproses input karena pada '"
							+ kalimat + "' ";
					pesan += "kata ";
					for (int i = 0; i < listKataTidakKetemu.size(); i++) {
						if (i != 0) {
							if (i < listKataTidakKetemu.size() - 1) {
								pesan += ", ";
							} else {
								pesan += " dan ";
							}
						}
						pesan += "'<b>" + listKataTidakKetemu.get(i) + "</b>'";
					}
					pesan += " tidak dikenali oleh sistem, silahkan ulangi pencarian dan pastikan input-an anda sudah benar" + info;
				} else if (listFoundGunung.size() == 0) {
					pesan += "Maaf, Sistem tidak dapat memproses input karena pada '"
							+ kalimat + "' ";
					pesan += "tidak terdapat kata objek pendakain gunung, silahkan ulangi pencarian" + info;
				} else if (listFoundGunung.size() > 4) {
					pesan += "Maaf sistem tidak dapat memproses input anda karena, kata objek pendakian yang dapat diproses sistem maksimal 4, silakan ulangi pencarian" + info;
				}
				break;
			case ERROR_SYNTAX:
				pesan += "Maaf, Sistem tidak dapat memahami input, karena berdasarkan aturan sintaksis dan simantik yang dimiliki sistem input tidak dapat diproses, silahkan ulangi pencarian" + info;
				break;
			default:
				break;
		}
		System.out.println("Pesan akhir " + pesan);
		return pesan;
	}

	//  : melakukan inisialisasi pengecekan sintaksis
	public boolean cekSinstaksis(List<OntoBahasa> resolusi) {
		long start = System.currentTimeMillis();
		// ditambah setelah modif preproses, 11Des14, jika input hanya kata kunci
		if (listFoundBahasa.size() == 0) {
			return true;
		}
		System.out.println("isi list bahasa : ");
		for (List<OntoBahasa> list : bahasas) {
			for (OntoBahasa bahasa : list) {
				bahasa.queryOtherDetail();
				System.out.println(bahasa);
			}
		}
		cekOntoGabung(resolusi);
		long end = System.currentTimeMillis();
		System.out.println("waktu untuk pengecekan = " + (end - start));
		if (resolusi.size() > 0) {
			return true;
		}
		return false;
	}

	private void cekOntoGabung(List<OntoBahasa> solusi) {
		Stack<List<OntoBahasa>> stacks = new Stack<List<OntoBahasa>>();
		int j = 0;
		System.out.println("****************");
		System.out.println("MULAI PENGECEKAN : " + listKataKetemu);
		while (j < bahasas.size()) {
			int i = j;
			boolean isAdditionalSkip = false;
			if (solusi.isEmpty()) {
				if (i < bahasas.size() - 1) {
					isAdditionalSkip = true;
					solusi.addAll(bahasas.get(i));
					i++;
				} else {
					break;
				}
			}

			List<OntoBahasa> baselist = new ArrayList<OntoBahasa>(solusi);
			List<OntoBahasa> list2 = bahasas.get(i);
			List<OntoBahasa> list1 = null;

			if (i < bahasas.size() - 1) {
				list1 = bahasas.get(i + 1);
			}

			solusi.clear();

			boolean ketemu = false;
			for (OntoBahasa b3 : baselist) {
				for (OntoBahasa b2 : list2) {
					if (list1 != null) {
						for (OntoBahasa b1 : list1) {
							System.out.println("\n----- Start cek 3 token-----"); //belum dipake untuk pengujian resmi
							System.out.println("[" + b1.getNilai() + ", " + b1.getShadowNilai() + ", " + b1.getKategori() + "] , ["
									+ b2.getNilai() + ", " + b2.getShadowNilai() + ", " + b2.getKategori() + "] , ["
									+ b3.getNilai() + ", " + b3.getShadowNilai() + ", " + b3.getKategori() + "]");
							if (SyntaxChecker.isValidSyntax(b1, b2, b3)) {
								ketemu = true;
								OntoBahasa hasil = SyntaxChecker
										.generateAccepted(b1, b2, b3);
								solusi.add(hasil);
								j++;
								j++;
								if (isAdditionalSkip) {
									j++;
								}
							}
						}
					}
					if (!ketemu) {
						System.out.println("\n----- Start cek 2 token-----");
						System.out.println("[" + b2.getNilai() + ", " + b2.getShadowNilai() + ", " + b2.getKategori() + "] , ["
								+ b3.getNilai() + ", " + b3.getShadowNilai() + ", " + b3.getKategori() + "]");
						if (SyntaxChecker.isValidSyntax(b2, b3)) {
							ketemu = true;
							OntoBahasa hasil = SyntaxChecker.generateAccepted(
									b2, b3);
							solusi.add(hasil);
							j++;
							if (isAdditionalSkip) {
								j++;
							}
						}
					}
				}
			}

			if (!ketemu) {
				System.out.println("FALSE");
				System.out.println("inser ke stack -> " + baselist);
				stacks.push(baselist);
				if (isAdditionalSkip) {
					j++;
				}
			}
		}
		checkStack(stacks, solusi);
	}

	private void checkStack(Stack<List<OntoBahasa>> stacks, List<OntoBahasa> solusi) {
		if (solusi.isEmpty()) {
			return;
		}

		List<OntoBahasa> baselist, list2 = null, list1;
		while (!stacks.isEmpty()) {
			list1 = list2;
			baselist = new ArrayList<OntoBahasa>(solusi);
			list2 = stacks.pop();

			solusi.clear();

			boolean ketemu = false;
			for (OntoBahasa b1 : baselist) {
				for (OntoBahasa b2 : list2) {
					if (list1 != null) {
						for (OntoBahasa b3 : list1) {
							if (SyntaxChecker.isValidSyntax(b1, b2, b3)) {
								ketemu = true;
								OntoBahasa hasil = SyntaxChecker
										.generateAccepted(b1, b2, b3);
								solusi.add(hasil);
							}
						}
					}

					if (!ketemu) {
						if (SyntaxChecker.isValidSyntax(b1, b2)) {
							ketemu = true;
							OntoBahasa hasil = SyntaxChecker.generateAccepted(
									b1, b2);
							solusi.add(hasil);
						}
					}
				}
			}

			if (ketemu) {
				list2 = null;
			} else {
				if (list1 != null) {
					return;
				}
			}
		}
	}


	// generate query akhir
	public List<List<OutputType>> generateInformasiAkhir(List<OntoBahasa> resolusi) {
		long start = System.currentTimeMillis();
		try {
			List<List<OutputType>> result = new ArrayList<List<OutputType>>();
			if (resolusi.size() == 0) {
				for (int i = 0; i < listFoundGunung.size(); i++) {
					String query = mountReader
							.getQueryDetailMount(listFoundGunung.get(i));
					List<OutputType> itemResult = mountReader
							.queryFinalAnswer(query);
					result.add(itemResult);
				}
				return result;
			} else {
				for (OntoBahasa bahasa : resolusi) {
					String query = bahasa.getFormatedVars();
					System.out.println("query hasil = " + query);
					List<OutputType> itemResult = mountReader
							.queryFinalAnswer(query);
					result.add(itemResult);
				}
				return result;
			}
		} finally { //akhirnya.... :)
			long end = System.currentTimeMillis();
			System.out.println("waktu diperlukan generate informasi = "
					+ (end - start));
		}
	}
}
