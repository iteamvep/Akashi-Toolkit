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
$data['object']['title']['zh_cn'] = "2017年秋刀鱼祭典";
$data['object']['content']['zh_cn'] = "开展为期约两周的秋刀鱼打捞活动，在特定海域可以获得秋刀鱼并完成相关任务。";
array_push($json, $data);

$data = null;
$data['type'] = $TYPE_URL;
$data['object']['title']['zh_cn'] = "详细攻略";
$data['object']['content']['zh_cn'] = "";
$data['object']['url'] = "https://zh.kcwiki.org/wiki/%E5%AD%A3%E8%8A%82%E6%80%A7/2017%E5%B9%B4%E7%A7%8B%E5%88%80%E9%B1%BC%E7%A5%AD%E5%85%B8#.E6.89.93.E6.8D.9E.E6.94.BB.E7.95.A5";
$data['object']['url_text']['zh_cn'] = "攻略:2017年秋刀鱼祭典";
array_push($json, $data);

$data = null;
$data['type'] = $TYPE_TITLE;
$data['object']['title']['zh_cn'] = "活动海域";
$data['object']['content']['zh_cn'] = "稀有掉落";
array_push($json, $data);

$data = null;
$data['type'] = $TYPE_CONTENT;
$data['object']['title']['zh_cn'] = "占守";
$data['object']['content']['zh_cn'] = "掉落点: 
3-3-G(Boss)
3-4-E(Boss)
3-5-K(Boss)

目前只有S胜利掉落报告
";
array_push($json, $data);

$data = null;
$data['type'] = $TYPE_CONTENT;
$data['object']['title']['zh_cn'] = "国后";
$data['object']['content']['zh_cn'] = "掉落点: 
3-2-H(Boss)
3-5-K(Boss)
6-5-J
6-5-M(Boss)

3-5-K(Boss)与6-5-M(Boss)存在A胜利掉落报告
";
array_push($json, $data);

$data = null;
$data['type'] = $TYPE_CONTENT;
$data['object']['title']['zh_cn'] = "择捉";
$data['object']['content']['zh_cn'] = "掉落点:
1-6-B
3-5-K(Boss)
6-1-K(Boss)

6-1-K(Boss)存在A胜利掉落报告
";
array_push($json, $data);

$data = null;
$data['type'] = $TYPE_CONTENT;
$data['object']['title']['zh_cn'] = "松轮";
$data['object']['content']['zh_cn'] = "掉落点:
1-5-I(Boss)
6-1-K(Boss)
6-5-M(Boss)

目前只有S胜利掉落报告。
根据官推，1-5掉落应为一号机限定
";
array_push($json, $data);

$data = null;
$data['type'] = $TYPE_URL;
$data['object']['title']['zh_cn'] = "秋刀鱼掉落统计";
$data['object']['content']['zh_cn'] = "POI2017年秋刀鱼活动统计";
$data['object']['url'] = "https://db.kcwiki.org/event-sanma2017";
$data['object']['url_text']['zh_cn'] = "2017年秋刀鱼祭典";
array_push($json, $data);

echo json_encode($json);