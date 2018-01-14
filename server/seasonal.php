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
$data['object']['summary']['zh_cn'] = "更多内容在“活动”中查看";
array_push($json, $data);

//wiki的舰娘ID
$data = null;
$data['type'] = $TYPE_GALLERY;
$data['not_safe'] = true;
$data['object']['title']['zh_cn'] = "新立绘";
$data['object']['urls'] = array(
"KanMusu079IllustSanma2017.png",
"KanMusu147IllustSanma2017.png",
"KanMusu165IllustSanma2017.png",
"KanMusu265IllustSanma2017.png",
"KanMusu280IllustSanma2017.png",
"KanMusu286IllustSanma2017.png",
"KanMusu292IllustSanma2017.png",
);
$data['object']['names'] = array(
"白露",
"Верный",
"卯月",
"鹿岛",
"狭雾",
"浦波",
"黎塞留",
);

//娇喘的舰娘ID
$data['object']['action_type'] = 1;
$data['object']['ids'] = array(
	42,
	147,
	165,
	465,
	480,
	486,
	492);
array_push($json, $data);
/*
$data = null;
$data['type'] = $TYPE_GALLERY;
$data['not_safe'] = true;
$data['object']['title']['zh_cn'] = "新深海";
$data['object']['urls'] = array(
"ShinkaiSeikan708.png",
"ShinkaiSeikan711.png",
);
$data['object']['names'] = array(
"水母水姬",
"深海海月姬",
);
array_push($json, $data);
*/
/*
$data = null;
$data['type'] = $TYPE_GALLERY;
$data['object']['title']['zh_cn'] = "新装备";
$data['object']['urls'] = array(
"Soubi194.png",
"Soubi195.png",
"Soubi196.png",
"Soubi197.png",
"Soubi198.png",
"Soubi199.png",
);
$data['object']['action_type'] = 2;
$data['object']['ids'] = array(
	194,
	195,
	196,
	197,
	198,
	199);
$data['object']['names'] = array(
"Laté 298B",
"SBD‎‎",
"TBD‎‎",
"F4F-3",
"F4F-4",
"SB2U",
);
array_push($json, $data);
*/

/*
$data = null;
$data['type'] = $TYPE_TITLE;
$data['object']['title']['zh_cn'] = "游戏更新";
$data['object']['title']['en'] = "Game update";
$data['object']['summary']['zh_cn'] = "2017年9月29日";
$data['object']['summary']['en'] = "Sept 29, 2017";
array_push($json, $data);
*/
/*
$data = null;
$data['type'] = $TYPE_GALLERY;
$data['not_safe'] = true;
$data['object']['title']['zh_cn'] = "新改二立绘";
$data['object']['title']['en'] = "New kai ni illustrations";
$data['object']['urls'] = array(
"KanMusu287Illust.png",
"KanMusu287DmgIllust.png",
);
$data['object']['names'] = array(
"鬼怒改二 全身图",
"鬼怒改二 中破全身图",
);
$data['object']['action_type'] = 1;
$data['object']['ids'] = array(
	487,
	487);
array_push($json, $data);
*/

/*
$data = null;
$data['type'] = $TYPE_CONTENT;
$data['object']['title']['zh_cn'] = "新任务";
$data['object']['title']['en'] = "New quest";
$data['object']['content']['zh_cn'] = "文字内容";
array_push($json, $data);

$data = null;
$data['type'] = $TYPE_CONTENT;
$data['object']['title']['zh_cn'] = "家具相关";
$data['object']['title']['en'] = "家具相关";
$data['object']['content']['zh_cn'] = "文字内容";
array_push($json, $data);
*/

/*$data = null;
$data['type'] = $TYPE_VOICE;
$data['not_safe'] = true;
$data['object']['title']['zh_cn'] = "限定语音";
$data['object']['title']['zh_cn'] = "Limited voices";
$data['object']['voices'] = array();

$data_voice_obj = null;
$data_voice_obj['zh'] = "唉嘛，终于还是到了这个季节啊…总是会，情不自禁地抬头望天呐…咦，敌军吗！？原来是瑞云啊。最近数量好像增加了呢…瑞云啊。";
$data_voice_obj['jp'] = "っうげー、ついにこの季節だよ…なんかもう、空見上げちゃうねーいつもさ…って、敵か！？んなんだ、瑞雲か。なんか最近増えたなー瑞雲。";
$data_voice_obj['scene'] = "长波";
$data_voice_obj['url'] = "135-Sec2Sanma2016.mp3";
array_push($data_voice['voice'], $data_voice_obj);

$data_voice_obj = null;
$data_voice_obj['zh'] = "下次的战斗，似乎要动真格了呢。我们是不是也要全力以赴呢…不得不好好准备一番了。妆容也要再画得精致一些才好，稍等哦。";
$data_voice_obj['jp'] = "今度の戦いは、少し大きそうね。私たちも全力出撃かしら、準備しなきゃね。お化粧も少しだけ気合なきゃ、待ってて。";
$data_voice_obj['scene'] = "陆奥";
$data_voice_obj['url'] = "002-Sec1Sanma2016.mp3";
array_push($data_voice['voice'], $data_voice_obj);

array_push($data['object']['voices'], $data_voice);
array_push($json, $data);*/

/*
$data = null;
$data['type'] = $TYPE_CONTENT;
$data['object']['title']['zh_cn'] = "其他更新";
$data['object']['title']['en'] = "Other updates";
$data['object']['content']['zh_cn'] = "文字内容";
array_push($json, $data);
*/

echo json_encode($json);
