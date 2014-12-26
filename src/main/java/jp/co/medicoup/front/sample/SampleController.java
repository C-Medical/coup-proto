package jp.co.medicoup.front.sample;

import java.util.HashMap;
import java.util.Map;

import jp.co.medicoup.base.front.provide.HtmlFragment;
import jp.co.medicoup.base.front.provide.PageResult;
import jp.co.medicoup.base.front.provide.annotation.GET;
import jp.co.medicoup.base.front.provide.annotation.Json;
import jp.co.medicoup.base.front.provide.annotation.POST;
import jp.co.medicoup.base.front.provide.annotation.Param;
import jp.co.medicoup.process.sample.dto.SampleDto;
import jp.co.medicoup.process.sample.service.SampleService;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleController {
	private static final Logger logger = LoggerFactory.getLogger(SampleController.class);

	@GET
	public PageResult sample1() {
		// HTMLのテンプレートに渡す変数マップ
		final Map<String, Object> variables = new HashMap<>();

		// HTMLのテンプレートで宣言した変数名とMapのキーを合わせればOK！
		variables.put("var1", "Some String!");
		variables.put("var2", 1234567890L);
		variables.put("var3", null);

		// DTOやそのコレクション等も変数にセット可能！
		variables.put("dtos", new SampleService().getSampleDtos());

		// HTMLのテンプレートの位置と埋め込む変数をセットした結果を返す
		return PageResult.withVariables("sample/sample1", variables);
	}

	@GET
	public PageResult sample2(@Param("id") long id) {
		// パラメータはメソッド引数で受け取れる！
		logger.debug("Parameter = {}", id);

		final Map<String, Object> variables = new HashMap<>();
		variables.put("id", id);
		return PageResult.withVariables("sample/sample2", variables);
	}

	@POST
	public PageResult sample2Post(@Param SampleDto dto) {
		// FormパラメータはDTOに変換された形で受けとれる
		logger.debug("Parameter = {}", dto);

		final Map<String, Object> variables = new HashMap<>();
		variables.put("id", dto.id);
		variables.put("date", DateTime.now());
		return PageResult.withVariables("sample/sample2", variables);
	}

	@POST
	public SampleDto sample2Ajax(@Param("id") long id, @Param @Json SampleDto dto) {
		// Ajaxのパラメータも同様で、JSONで送信されたパラメータはDTOで受け取れる
		logger.debug("Parameter = {} & {}", id, dto);
		return null;
	}

	@GET
	public HtmlFragment sample2Fragment() {
		// HTMLフラグメントも返せるよ
		return null;
	}
}
