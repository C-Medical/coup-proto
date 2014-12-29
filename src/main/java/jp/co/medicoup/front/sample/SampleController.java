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

/**
 * このコントローラーはフレームワークによって自動的に以下のURLにマッピングされます。<br>
 * URLはパッケージ名、クラス名、メソッド名によって構成されます。
 * <ul>
 * <li>戻り値が{@link PageResult}のメソッド : /page/sample/sample/メソッド名
 * <li>上記以外のメソッド : /ajax/sample/sample/メソッド名
 *
 *
 * @author izumi_j
 *
 */
public final class SampleController {
    private static final Logger logger = LoggerFactory.getLogger(SampleController.class);

    @GET
    public PageResult sample1() {
        // HTMLのテンプレートに渡す変数マップ
        final Map<String, Object> variables = new HashMap<>();

        // HTMLのテンプレートで宣言した変数名とMapのキーを合わせればOK！
        variables.put("var1", "Some String!");
        variables.put("var2", 1234567890L);
        variables.put("var3", DateTime.now());

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
        variables.put("dto", new SampleService().getSampleDto(id));
        return PageResult.withVariables("sample/sample2", variables);
    }

    @POST
    public PageResult sample2Post(@Param SampleDto dto) {
        // FormパラメータはDTOに変換された形で受けとれる
        logger.debug("Parameter = {}", dto);

        final Map<String, Object> variables = new HashMap<>();
        dto.date = DateTime.now();
        variables.put("dto", dto);
        variables.put("posted", true);
        return PageResult.withVariables("sample/sample2", variables);
    }

    @GET
    public PageResult sample3(@Param("redirect") boolean redirect) {
        final Map<String, Object> variables = new HashMap<>();
        if (redirect) {
            // リダイレクトもできるよ
            variables.put("id", String.valueOf(123L));
            return PageResult.redirectTo("/page/sample/sample/sample4", variables);
        } else {
            return PageResult.withVariables("sample/sample3", variables);
        }
    }

    @GET
    public PageResult sample4() {
        return PageResult.withVariables("sample/sample4", null);
    }

    @POST
    public SampleDto sample4Ajax(@Param("hoge") String hoge, @Param("dto") @Json SampleDto dto) {
        // Ajaxのパラメータも同様で、JSONで送信されたパラメータはDTOで受け取れる
        logger.debug("Parameter = {} & {}", hoge, dto);
        dto.date = DateTime.now();
        return dto;
    }

    @GET
    public HtmlFragment sample4Fragment() {
        // HTMLフラグメントも返せるよ
        final Map<String, Object> variables = new HashMap<>();
        variables.put("dto", new SampleService().getSampleDto(5));
        return HtmlFragment.create("sample/sample4-fragmet", variables);
    }
}
