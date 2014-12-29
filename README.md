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
