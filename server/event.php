<?php
$api_version = $_GET['api_version'];

$TYPE_TITLE = 0;
$TYPE_GALLERY = 1;
$TYPE_CONTENT = 2;
$TYPE_URL = 3;
$TYPE_VOICE = 4;

$json = array();

$data = null;
$data['type'] = $TYPE_TITLE;
$data['object']['title']['zh_cn'] = "2018年冬季活动";
$data['object']['content']['zh_cn'] = "捷号決戦！邀撃、レイテ沖海戦(後篇)";
array_push($json, $data);

$data = null;
$data['type'] = $TYPE_URL;
$data['object']['title']['zh_cn'] = "详细攻略";
$data['object']['content']['zh_cn'] = "";
$data['object']['url_text']['zh_cn'] = "kcwiki:2018年冬季活动";
$data['object']['url'] = "https://zh.kcwiki.org/wiki/2018%E5%B9%B4%E5%86%AC%E5%AD%A3%E6%B4%BB%E5%8A%A8";
array_push($json, $data);

$data = null;
$data['type'] = $TYPE_TITLE;
$data['object']['title']['zh_cn'] = "活动原型";
$data['object']['content']['zh_cn'] = "莱特湾海战";
array_push($json, $data);

$data = null;
$data['type'] = $TYPE_CONTENT;
$data['object']['title']['zh_cn'] = "概述";
$data['object']['content']['zh_cn'] = "莱特湾海战发生于1944年10月20日，到10月26日结束，是当时发生在菲律宾周围日本联合舰队与美国海军和澳大利亚海军组成的联合国军之间一系列的海战的总称。这些海战从前到后可以大概分为：巴拉望水道海战、锡布延海战、苏里高海峡海战、恩加尼奥角海战、萨马岛海战。

上下6天之内，在这场美军称作KING II行动，日方称作捷一号作战的海战中。日本联合舰队投入了尚能执行任务的舰艇中的多数；美国海军也投入了其在太平洋上活动的大半军舰。双方总计投入21艘航空母舰、21艘战列舰、170艘驱逐舰与近2,000架飞机。因此，莱特湾海战凭借其规模之大、参战舰艇之多、作战范围之广，获得了史上最大规模海战的称呼。
";
array_push($json, $data);

$data = null;
$data['type'] = $TYPE_URL;
$data['object']['title']['zh_cn'] = "掉落统计";
$data['object']['content']['zh_cn'] = "POI2018年冬季活动统计";
$data['object']['url'] = "https://db.kcwiki.org/drop/";
$data['object']['url_text']['zh_cn'] = "poi-statistics掉落统计";
array_push($json, $data);

echo json_encode($json);