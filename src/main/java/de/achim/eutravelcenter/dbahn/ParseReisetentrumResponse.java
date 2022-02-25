package de.achim.eutravelcenter.dbahn;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseReisetentrumResponse {
	
	private static final String RESISEAUSKUNFT_RESPONSE_HTML = "resiseauskunft_response.html";

	public Document getDocFromResourceFile (String filename) throws IOException {
		File ff = null;
		try {
			ff = new File(this.getClass()
					.getClassLoader()
					.getResource(filename)
					.toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(ff == null) {
			throw new IllegalArgumentException(filename + " not found");
		}


		return Jsoup.parse(ff, "UTF-8");
	}

	public Elements parseReiseauskuftResponse(Document doc) {
		/*
		 * tbody id="overview_upateC0-4"
		 * tbody id="overview_updateC0-5"		 
		 */
	
		String searchexpr = "tbody[id~=^overview_updateC0-[0-9]?]";
		Elements results = doc.select(searchexpr);
		return results;
		
	}
	
	public List<String> findDetailsLinks(Elements els) {
		
		/*
		 *  <tr class="buttonLine">
            <td data-concol="1">
            <a class="floatLeft iconLink open" id="linkDtlC0-4" href="https://reiseauskunft.bahn.de/bin/query.exe/dn?ld=4399&amp;protocol=https:&amp;seqnr=1&amp;ident=7i.02888599.1645435080&amp;rt=1&amp;rememberSortType=minDeparture&amp;" rel="HWAI:CONNECTION{C0-4}[id=C0-4][HwaiConId=C0-4][HwaiDetailStatus=details];~CONNECTION{C0-4}[HwaiMapStatus][HwaiMapNumber][HwaiMapSlider][HwaiDetailHimMessage]" title="Details einblenden"><span><span></span></span>Details einblenden</a></td>
            <td class="time" data-concol="3"></td>
            <td colspan="3" class="return lastrow button
		 */
		List<String> dataList = new ArrayList<>();
		for (Element el : els) {
			Element result = el.select("a").first();
			String data = result.attr("href");
			dataList.add(data);
		}
		return dataList;
		
	}
	
	public List<String> getDataList(Elements els) {
		List<String> dataList = new ArrayList<>();
		for (Element result : els) {
			Element sibling = this.getPreviousSibling(result);
			dataList.add(this.getDatafromSibling(sibling));
		}
		return dataList;
	}
	
	public String getDatafromSibling(Element el) {
		return el.data();
	}
	
	public Element getPreviousSibling(Element el) {
		return el.previousElementSibling();
	}
	
	public static void main(String[] args) throws Exception {
		ParseReisetentrumResponse pRR = new ParseReisetentrumResponse();
		Document doc = pRR.getDocFromResourceFile(RESISEAUSKUNFT_RESPONSE_HTML);
		Elements tbodies = pRR.parseReiseauskuftResponse(doc);
		List<String> dataList = pRR.getDataList(tbodies);
		System.out.println("********" + dataList.size());
		List<String> detailsLinkList = pRR.findDetailsLinks(tbodies);
		System.out.println("********" + detailsLinkList.size());
		System.out.println(detailsLinkList.get(0));
	}

}
