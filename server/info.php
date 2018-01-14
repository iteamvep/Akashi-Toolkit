<?php
$api_version = $_GET['api_version'];
$api_channel = $_GET['api_channel'];

$NOT_DISMISSIBLE = 1 << 0;
$HTML_CONTENT = 1 << 1;
$ACTION_VIEW_BUTTON = 1 << 2;
$COUNT_DOWN = 1 << 3;

$json['messages'] = array();

$update_stable['versionCode'] = 426;
$update_stable['versionName'] = "0.13.4";
$update_stable['url'] = "https://pek3a.qingstor.com/kcwiki/akashitoolkit-".$update_stable['versionCode'].".apk";
$update_stable['url2'] = "https://pek3a.qingstor.com/kcwiki/akashitoolkit-".$update_stable['versionCode']."-firebase.apk";
$update_stable['change']['zh_cn'] = "• 更新数据";
$update_stable['change']['zh_tw'] = "• 更新数据";
$update_stable['change']['en'] = "• Update data";

$update_latest['versionCode'] = 423;
$update_latest['versionName'] = "0.13.4";
$update_latest['url'] = "https://pek3a.qingstor.com/kcwiki/akashitoolkit-".$update_latest['versionCode'].".apk";
$update_latest['url2'] = "https://pek3a.qingstor.com/kcwiki/akashitoolkit-".$update_latest['versionCode']."-firebase.apk";
$update_latest['change']['zh_cn'] = "• 更新数据
• BUG 修复和 UI 改进";
$update_latest['change']['zh_tw'] = "• 更新数据
• BUG 修复和 UI 改进";
$update_latest['change']['en'] = "• Data update
• BUG fix and UI improvement";

if ($update_stable['versionCode'] >= $update_latest['versionCode']) {
	$update_latest = $update_stable;
}

$message = null;
$message['title']['zh_cn'] = "关于再次被下架";
$message['message']['zh_cn'] = "似乎二次元+有一点=直接枪毙，所以这次又💊。\n我们在重新上架了一个没有图片的版本，您可以选择下载该版本，或者从官网下载完整版。";
$message['title']['en'] = "Note of be removed again";
$message['message']['en'] = "We have republished a non-image version on Google Play again, you can donwload that version or download full version from our website.";
$message['max_api'] = 1;
$message['only_play'] = true;
$message['show_first'] = false;
$message['action_name']['zh_cn'] = "访问官网";
$message['action_name']['en'] = "View homepage";
$message['link'] = "http://app.kcwiki.org/";
$message['type'] = $NOT_DISMISSIBLE | $ACTION_VIEW_BUTTON;
array_push($json['messages'], $message);

/*$message = null;
$message['title']['zh_cn'] = "test 2";
$message['message']['zh_cn'] = "test";
$message['title']['en'] = "test 2";
$message['message']['en'] = "test";
$message['min_api'] = 2;
$message['min_version'] = 413;
$message['only_play'] = false;
$message['show_first'] = false;
array_push($json['messages'], $message);*/
/*
$message = null;
$message['title']['zh_cn'] = "更新计划 (看起来要无限延期了)";
$message['message']['zh_cn'] = "哎呀哎呀.. 拖到了现在还是什么也没做（

为什么这么久不更新呢 因为 Rikka 忙着去做别的东西 是看起来收益更高的东西（（（

• 说起来感觉任务那块好像好难用 要不要改掉呢
• 装备详情内容塞进 RecyclerView
• 加入全部舰娘的限定立绘
• 舰队模拟加入索敌计算（好像很麻烦的样子）
• 舰队模拟从工具中移出
• 加入一直在喊的建造公式什么的?
• 让远征计时有更易理解的 UI
• 任务数据格式更换 / 多语言支持
• 检测到有安装 Google Play services 的时候推荐使用 firebase 的那套东西的版本";
*/
/*
$message = null;
$message['title']['zh_cn'] = "更新计划 (暂时由某萌新维护)";
$message['message']['zh_cn'] = "review代码中。。。暂时把容易报错的模块关掉了orz";

 array_push($json['messages'], $message);

if ($api_channel == 0) {
	$json['update'] = $update_stable;
} else {
	$json['update'] = $update_latest;
}
 */

echo json_encode($json);