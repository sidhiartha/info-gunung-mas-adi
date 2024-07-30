package servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

/**
 * Servlet implementation class WordsChecker
 */
public class WordsChecker extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public boolean isError;

	public List<Object> question = new ArrayList<Object>();
	public String[] realQuestion;
	public List<Integer> rightWords;

	/**
	 * Default constructor.
	 */
	public WordsChecker() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// @Override
	// protected void doGet(HttpServletRequest request,
	// HttpServletResponse response) throws ServletException, IOException {
	// // TODO Auto-generated method stub
	// String name = request.getParameter("word");
	// String buffer =
	// "<select id=\"suggest\" style=\"overflow: auto; height: auto\">";
	//
	// checkSpelling(name);
	//
	// if (isError) {
	// lastCheck();
	// for (int i = 0; i < question.size(); i++) {
	// buffer += "<option>" + question.get(i).toString() + "</option>";
	// }
	// } else {
	// buffer += "<option>" + name + "</option>";
	// }
	//
	// buffer = buffer + "</select>";
	// response.getWriter().println(buffer);
	// }

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String name = request.getParameter("word");
		String buffer = "";

		checkSpelling(name);
		int indexRight = 0;
		JSONObject json = new JSONObject();

		try {
			if (isError) {
				lastCheck();
				for (int i = 0; i < question.size(); i++) {
					buffer = question.get(i).toString();
					json.append("Key " + i, buffer);
				}
			} else {
				buffer = name;
				json.append("Key " + indexRight, buffer);
				indexRight++;
			}
		} catch (JSONException jse) {

		}
		System.out.println("json = " + json);
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	public void checkSpelling(String words) {
		// TODO Auto-generated constructor stub
		isError = false;
		question.clear();
		System.out.println("" + words);
		realQuestion = words.split(" ");
		rightWords = new ArrayList<Integer>();
		for (int i = 0; i < realQuestion.length; i++) {
			rightWords.add(i);
		}
		try {
			SpellDictionary dictionary = new SpellDictionaryHashMap(new File(
					"bahasa.0"));
			SpellChecker spellChecker = new SpellChecker(dictionary);
			spellChecker.addSpellCheckListener(new SuggestionListener());
			spellChecker.checkSpelling(new StringWordTokenizer(words));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void lastCheck() {
		while (rightWords.size() > 0) {
			for (int id = 0; id < question.size(); id++) {
				Object temp = question.get(id);
				question.set(id, temp + " " + realQuestion[rightWords.get(0)]);
			}
			rightWords.remove(0);
		}
	}

	public class SuggestionListener implements SpellCheckListener {
		@Override
		public void spellingError(SpellCheckEvent event) {
			isError = true;
			// System.out.println("Misspelling: " + event.getInvalidWord());
			int errorPos = -1;
			for (int i = 0; i < realQuestion.length; i++) {
				if (event.getInvalidWord().equals(realQuestion[i])) {
					errorPos = i;
					break;
				}
			}
			int numRW;
			while ((numRW = rightWords.get(0)) < errorPos) {
				if (question.isEmpty()) {
					question.add(realQuestion[numRW]);
				} else {
					for (int id = 0; id < question.size(); id++) {
						Object temp = question.get(id);
						question.set(id, temp + " " + realQuestion[numRW]);
					}
				}
				rightWords.remove(0);
			}
			rightWords.remove((Integer) errorPos);
			List<Object> suggestions = event.getSuggestions();
			if (suggestions.isEmpty()) {
				// System.out.println("No suggestions found.");
				if (question.isEmpty()) {
					question.add(event.getInvalidWord());
				} else {
					for (int id = 0; id < question.size(); id++) {
						Object temp = question.get(id);
						question.set(id, temp + " " + event.getInvalidWord());
					}
				}
			} else {
				// System.out.print("Suggestions: ");
				if (question.isEmpty()) {
					for (Iterator<Object> i = suggestions.iterator(); i
							.hasNext();) {
						question.add(i.next());
					}
				} else {
					List<Object> temp = new ArrayList<Object>();
					temp.addAll(question);
					question.clear();
					for (int id = 0; id < temp.size(); id++) {
						for (Iterator i = suggestions.iterator(); i.hasNext();) {
							question.add(temp.get(id) + " " + i.next());
						}
					}
				}
			}
		}
	}
}
