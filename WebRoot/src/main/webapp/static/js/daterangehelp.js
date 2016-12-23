function cb(start, end) {
  $('#reportrange').find('span').html(start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD'));
}

var startDate = moment();
var endDate = moment();
cb(startDate, endDate);

var picker = $('#reportrange').daterangepicker({
  startDate: startDate,
  endDate: endDate,
  format: 'YYYY-MM-DD',
  separator: '-',
  locale: {
    applyLabel: '确定',
    cancelLabel: '取消',
    fromLabel: '从',
    toLabel: '到',
    customRangeLabel: '自定义...',
    daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
    monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
    firstDay: 0
  },
  ranges: {
    '今天': [moment(), moment()],
    '昨天': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
    '最近7天': [moment().subtract(6, 'days'), moment()],
    '最近30天': [moment().subtract(29, 'days'), moment()],
    '本月': [moment().startOf('month'), moment().endOf('month')],
    '上月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
  }
}, cb);