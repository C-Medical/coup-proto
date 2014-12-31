プロトタイプ開発手順
==========

## [開発環境]

* **Gradleをインストール**

https://www.gradle.org/  
※Android Studioインストール済の場合、\android-studio\gradle\gradle-2.2.1\binにPathを通せば良い


* **EclipseにGoogleプラグインをインストール**

https://dl.google.com/eclipse/plugin/4.x  
ヘルプ→新規ソフトウェアのインストールで、上記URLからダウンロード


* **coup-protoをEclipseにインポート**

clone後、coup-protoディレクトリで、`gradle eclipse`コマンドを実行  
成功したら、ファイル→インポート→一般→既存プロジェクトを...で、Eclipseに取り込む


* **プロジェクトの設定**

coup-protoプロジェクトのプロパティ→Google→Web Applicationで、  
`warディレクトリ有`にチェックを入れ、`webapp`ディレクトリを指定


* **ローカルで実行**

例えば、coup-protoプロジェクトを右クリック→実行→Web Application（Googleマークの）  
ブラウザからlocalhost:8888にアクセス！


## [画面を作ってみる]

*以下の仕様は仮です！*

* **HTMLファイルを作る**

`webapp/WEB-INF/templage`ディレクトリ以下に、**.htmlファイルを作る  
※ヘッダ部等は、envelope.htmlに定義されているので、コンテンツのみを実装するイメージで

* **ThymeleafやBootstrapを勉強する**

[Thymeleaf](http://www.thymeleaf.org/doc/usingthymeleaf.html)  
[Bootstrap](http://getbootstrap.com/)  
あたりを見て、HTMLを書いてみる

* **Controllerを作る**

`jp.co.medicoup.front`パッケージ以下に、**Controllerという命名規則でクラスを作る  
※URLはパッケージ名、クラス名、メソッド名から自動で決定される！  
e.g jp.co.medicoup.front.sample.SampleController#hoge >> localhost:8888/page/sample/sample/hoge

* **ページを返すメソッドを作る**

`jp.co.medicoup.base.front.provide.PageResult`を返すメソッドを作る  
※戻り値に作成したHTMLファイルの場所を指定する！

* **Controllerを登録**

`jp.co.medicoup.registry.ControllerRegistry`に作成したControllerを登録する
※この仕組み、要検討！

