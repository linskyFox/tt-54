//Load common code that includes config, then load the app logic for this page.
require(['./common'], function (common) {
	require(['./app/member/controller/dropship']);
});