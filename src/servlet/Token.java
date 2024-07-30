package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import models.ApiResponse;
import models.OntoBahasa;
import models.OutputType;

import org.json.JSONArray;

import controller.Preprocessor;
import engines.CuacaChecker;

/**
 * Servlet implementation class Token
 */
public class Token extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Token() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */

    // menghilangkan spasi lebih dari satu diatara kata
    List<String> GetStringArrayFromString(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        String[] words = sentence.split(" ");
        // String[] result =
        for (int i = 0; i < words.length; i++) {
            if (words[i].trim().length() > 0) {
                result.add(words[i].trim());
            }
        }
        System.out.println("setelah handling = " + result);
        return result;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        // Terima Input
        String kalimat = request.getParameter("keyword");
        System.out.println("sebelum replace = " + kalimat);
        kalimat = kalimat.replaceAll("[^a-zA-Z0-9, ]", ""); // menghilangkan karakter non numerik
        System.out.println("setelah replace = " + kalimat);
        kalimat = kalimat.toLowerCase();

        // handling input spasi
        if (kalimat.trim().isEmpty()) {
            String referer = request.getHeader("Referer");
            System.out.println(referer);
            response.sendRedirect(referer);
            return;
        }

        // Validasi input
        Preprocessor preprocessor = new Preprocessor(getServletContext());
        boolean isValid = preprocessor
                .CheckIsSentenceValid(GetStringArrayFromString(kalimat));
        if (!isValid) {
            PrintWriter out = response.getWriter();

            ApiResponse result = new ApiResponse();
            result.output = preprocessor
                    .generateResponseMessage(
                            Preprocessor.ResponseType.ERROR_INPUT, kalimat);
            result.errorCode = Preprocessor.ResponseType.ERROR_INPUT.ordinal();

            out.println(result.toJson());

            out.flush();
            return;
        }

        List<OntoBahasa> resolusi = new ArrayList<OntoBahasa>();

        // jika input valid cek bahasa
        boolean sitaksValid = preprocessor.cekSinstaksis(resolusi);
        if (!sitaksValid) {
            PrintWriter out = response.getWriter();

            ApiResponse result = new ApiResponse();
            result.output = preprocessor
                    .generateResponseMessage(
                            Preprocessor.ResponseType.ERROR_SYNTAX, kalimat);
            result.errorCode = Preprocessor.ResponseType.ERROR_SYNTAX.ordinal();

            out.println(result.toJson());
            out.flush();
            return;
        }

        // jika cek bahasa valid tampilkan informasi
        long start2 = System.currentTimeMillis();
        List<List<OutputType>> outputResults = preprocessor
                .generateInformasiAkhir(resolusi);
        request.setAttribute("input", kalimat);

        if (getStringDesk(outputResults).isEmpty()
                && getStringMaps(outputResults).isEmpty()
                && getStringPmId(outputResults).isEmpty()
                && getCuaca(outputResults).isEmpty()) {
            PrintWriter out = response.getWriter();

            ApiResponse result = new ApiResponse();
            result.output = "maaf, sistem tidak memiliki pengetahuan tentang informasi yang anda minta, silakan ulangi pencarian";

            out.println(result.toJson());
            out.flush();
//		 	return;
        } else {
            PrintWriter out = response.getWriter();
            ApiResponse result = new ApiResponse();

            result.output = getStringDesk(outputResults);
            result.maps = new JSONArray(getStringMaps(outputResults));
            result.pmId = new JSONArray(getStringPmId(outputResults));
            result.cuaca = getCuaca(outputResults);

            String resultString = result.toJson();

            System.out.println(resultString);

            out.println(resultString);
            out.flush();
        }

        long end = System.currentTimeMillis();
        System.out.println("waktu diperlukan kirim data = " + (end - start2));
        System.out.println("waktu diperlukan semua proses = " + (end - start));
    }

    String getStringDesk(List<List<OutputType>> source) {
        String result = "";

        for (List<OutputType> sublist : source) {
            for (OutputType outputType : sublist) {
                if (!outputType.labelStr.equals("")) {
                    result += outputType.labelStr + "<br>";
                }
                if (!outputType.deskripsiStr.equals("")) {
                    result += outputType.deskripsiStr + "<br>";
                }
                if (!outputType.lokasiStr.equals("")) {
                    result += outputType.lokasiStr + "<br>";
                }
                if (!outputType.telepon.equals("")) {
                    result += outputType.telepon + "<br>";
                }
                if (outputType.elevasiInt > 0) {
                    result += outputType.elevasiInt
                            + " mdpl *berdasarkan data GPS Garmin 60S" + "<br>";
                }
                if (outputType.jarakInt > 0) {
                    result += outputType.jarakInt
                            + " meter *koordinat berdasarkan GPS Garmin 60S, dihitung menggunakan Haversine Formula"
                            + "<br>";
                }
            }
        }
        //System.out.println("Hasil : \n" + result);
        return result;
    }

    List<String> getStringPmId(List<List<OutputType>> source) {
        List<String> result = new ArrayList<String>();

        for (List<OutputType> sublist : source) {
            for (OutputType outputType : sublist) {
                if (!outputType.oIdJson.equals("")) {
                    result.add(outputType.oIdJson);
                }
                if (!outputType.jIdJson.equals("")) {
                    result.add(outputType.jIdJson);
                }
                if (!outputType.gIdJson.equals("")) {
                    result.add(outputType.gIdJson);
                }
                if (!outputType.pmIdJson.equals("")) {
                    result.add(outputType.pmIdJson);
                }
            }
        }
        return result;
    }

    List<String> getStringMaps(List<List<OutputType>> source) {
        List<String> result = new ArrayList<String>();

        for (List<OutputType> sublist : source) {
            for (OutputType outputType : sublist) {
                if (!outputType.OmapJson.equals("")) {
                    result.add(outputType.OmapJson);
                }
                if (!outputType.JmapJson.equals("")) {
                    result.add(outputType.JmapJson);
                }
                if (!outputType.mapJson.equals("")) {
                    result.add(outputType.mapJson);
                }
                if (!outputType.GmapJson.equals("")) {
                    result.add(outputType.GmapJson);
                }
            }
        }
        return result;
    }

    String getCuaca(List<List<OutputType>> source) {
        String result = "";
        CuacaChecker cuacaChecker = null;
        for (List<OutputType> subList : source) {
            for (OutputType outputType : subList) {
                if (!outputType.cuacaNama.equals("")
                        && !outputType.cuacaFile.equals("")) {
                    if (cuacaChecker == null) {
                        cuacaChecker = new CuacaChecker();
                    }
                    cuacaChecker.readData(outputType.cuacaFile,
                            outputType.cuacaNama);
                    result += cuacaChecker.getKota()
                            + "<br>"
                            + "&nbsp;&nbsp;&nbsp;&nbsp;Tanggal : "
                            + cuacaChecker.getTglMulai()
                            + " sampai "
                            + cuacaChecker.getTglSelesai()
                            + "<br> "
                            + "&nbsp;&nbsp;&nbsp;&nbsp;Cuaca : "
                            + cuacaChecker.getCuaca()
                            + "<br>"
                            + "&nbsp;&nbsp;&nbsp;&nbsp;Kelembaban Max : "
                            + cuacaChecker.getKelembapanMax()
                            + "<br>"
                            + "&nbsp;&nbsp;&nbsp;&nbsp;Kelembaban Min : "
                            + cuacaChecker.getKelembapanMin()
                            + "<br>"
                            + "&nbsp;&nbsp;&nbsp;&nbsp;Suhu Max : "
                            + cuacaChecker.getSuhuMax()
                            + "<br>"
                            + "&nbsp;&nbsp;&nbsp;&nbsp;Suhu Min : "
                            + cuacaChecker.getSuhuMin()
                            + "<br>"
                            + "&nbsp;&nbsp;&nbsp;&nbsp;Kecepatan Angin : "
                            + cuacaChecker.getKecepatanAngin()
                            + "<br>"
                            + "&nbsp;&nbsp;&nbsp;&nbsp;<sup>*) Beradasarkan balai "
                            + cuacaChecker.getBalai() + "</sup>" + "<br><br>";
                }
            }
        }
        return result;
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
    }
}
