<template>
  <!--分析页-->
  <div class="content">
    <Row class="txt-left" :gutter="12">
      <Col span="6">
        <Card>
          <p class="title">总销售额</p>
          <a href="javascript:" slot="extra">
            <Tooltip content="指标说明" placement="top">
              <Icon type="ios-information-circle-outline"/>
            </Tooltip>
          </a>
          <p class="num">￥ 189,345</p>
          <div class="des">
            同周比 12%
            <Icon type="md-arrow-dropup" color="#f5222d"/>
            日环比 11%
            <Icon type="md-arrow-dropdown" color="#52c41a"/>
          </div>
          <Divider/>
          <p class="footer">日均销售额 ￥234.56</p>
        </Card>
      </Col>
      <Col span="6">
        <Card>
          <p>总销售额</p>
          <a href="javascript:" slot="extra">
            <Tooltip content="指标说明" placement="top">
              <Icon type="ios-information-circle-outline"/>
            </Tooltip>
          </a>
          <p class="num">￥ 189,345</p>
          <div id="chart"></div>
          <Divider class="chart_divider"/>
          <p>日访问量 123,4</p>
        </Card>
      </Col>
      <Col span="6">
        <Card>
          <p>总销售额</p>
          <a href="javascript:" slot="extra">
            <Tooltip content="指标说明" placement="top">
              <Icon type="ios-information-circle-outline"/>
            </Tooltip>
          </a>
          <p class="num">￥ 189,345</p>
          <div id="myChart"></div>
          <Divider class="chart_divider"/>
          <p>转化率 60%</p>
        </Card>
      </Col>
      <Col span="6">
        <Card>
          <p>运营活动效果</p>
          <a href="javascript:" slot="extra">
            <Tooltip content="指标说明" placement="top">
              <Icon type="ios-information-circle-outline"/>
            </Tooltip>
          </a>
          <p class="num">73%</p>
          <Progress :percent="60" status="success" hide-info class="progress"/>
          <Divider/>
          <div class="footer">
            同周比 12%
            <Icon type="md-arrow-dropup" color="#f5222d"/>
            日环比 11%
            <Icon type="md-arrow-dropdown" color="#52c41a"/>
          </div>
        </Card>
      </Col>
    </Row>
    <div class="tab">
      <Tabs>
        <div class="extra-wrap" slot="extra">
          <div class="extra-item">
            <a>今日</a>
            <a>本周</a>
            <a>本月</a>
            <a>本年</a>
          </div>
          <DatePicker type="daterange" placement="bottom-end" placeholder="Start date - End date"></DatePicker>
        </div>
        <TabPane label="用户统计">
          <Row>
            <Col span="16">
              <p class="title">用户增长趋势</p>
              <div id="sale"></div>
            </Col>
            <Col span="8">
              <p class="title">课程销售排行榜</p>
              <rakinglist :list="rankList"></rakinglist>
            </Col>
          </Row>
        </TabPane>
        <TabPane label="VIP统计">
          <Row>
            <Col span="16">
              <p class="title">VIP开通趋势</p>
              <div id="scan"></div>
            </Col>
            <Col span="8">
              <p class="title">门店销售排行榜</p>
              <rakinglist :list="rankList"></rakinglist>
            </Col>
          </Row>
        </TabPane>
      </Tabs>
    </div>
    <Row :gutter="15" class-name="bottom">
      <Col span="12">
        <Card>
          <p slot="title">热门搜索</p>
          <Row :gutter="25">
            <Col span="12">
              <div class="num-info">
                <span class="title">搜索用户数</span>
                <div class="value">
                  <span class="total">12321</span>
                  <span class="subtotal">71.2 <Icon type="md-arrow-dropup" color="#f5222d"/></span>
                </div>
              </div>
              <div id="search"></div>
            </Col>
            <Col span="12">
              <div class="num-info">
                <span class="title">搜索用户数</span>
                <div class="value">
                  <span class="total">12321</span>
                  <span class="subtotal">71.2 <Icon type="md-arrow-dropdown" color="#f5222d"/></span>
                </div>
              </div>
              <div id="search1"></div>
            </Col>
          </Row>
          <Table border border :columns="columns5" :data="data5"></Table>
          <div class="pageContent">
            <Page :total="100"/>
          </div>
        </Card>
      </Col>
      <Col span="12">
        <Card>
          <p slot="title">销售额占比</p>
          <a href="javascript:" slot="extra">
            <Icon type="ios-more"/>
          </a>
          <div>
            <ButtonGroup size="large">
              <Button>全渠道</Button>
              <Button>线上</Button>
              <Button>门店</Button>
            </ButtonGroup>
          </div>
          <div id="pie"></div>
        </Card>
      </Col>
    </Row>
  </div>
</template>

<style scoped lang="less">
.content {
  background-color: #f0f2f5;
  text-align: left;
}

.bottom {
  margin-top: 15px;
}

.pageContent {
  text-align: right;
  padding-top: 20px;
}

.num-info {
  .title {
    color: rgba(0, 0, 0, .45);
    font-size: 14px;
    height: 22px;
    line-height: 22px;
    overflow: hidden;
    text-overflow: ellipsis;
    word-break: break-all;
    white-space: nowrap;
  }

  .value {
    .total {
      color: rgba(0, 0, 0, .85);
      display: inline-block;
      line-height: 32px;
      height: 32px;
      font-size: 24px;
      margin-right: 32px;
    }

    .subtotal {
      color: rgba(0, 0, 0, .45);
      font-size: 16px;
      vertical-align: top;
      margin-right: 0;

      i {
        font-size: 22px;
        color: red;
        transform: scale(.82);
        margin-left: 4px;
      }
    }
  }
}

#chart, #myChart {
  width: 100%;
  height: 100px;
  margin: auto;
  top: -10px;
}

#search, #search1 {
  height: 100px;
}

#pie {
  height: 452px;
}

#sale, #scan {
  height: 300px;
}

.ivu-divider-horizontal {
  margin: 10px 0;
}

.txt-left {
  text-align: left;

  .title, a {
    color: rgba(0, 0, 0, .45);
    font-size: 14px;
  }

  .num {
    font-size: 30px;
    color: #000;
    margin-bottom: 10px;
  }

  .des, .footer {
    color: rgba(0, 0, 0, .65);
    font-size: 14px;

    i {
      font-size: 22px;
      margin-right: 10px;
    }
  }

  .des {
    margin-top: 23px;
  }

  .chart_divider {
    margin-top: -55px;
  }

  .progress {
    margin: 8px 0;
  }
}

.tab {
  margin-top: 15px;
  padding: 20px 10px 15px;
  background-color: #fff;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }

  .title {
    font-size: 14px;
    color: rgba(0, 0, 0, 0.85);
    text-align: left;
  }
}

</style>
