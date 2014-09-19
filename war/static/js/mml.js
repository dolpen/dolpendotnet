MML = function(playBtn, stopBtn, textArea, status){
    this.obj = new JSMML();
    this.timer = null;
    this.playBtn = playBtn;
    this.stopBtn = stopBtn;
    this.textArea = textArea;
    this.status = status;
    this.tm = 0;
    this.setButtonFunc();
    this.unsetTimer();
};

MML.prototype.setTimer = function(){
    this.unsetTimer();
    var self = this;
    this.tm = 0;
    var timerFunc = function(){
        self.status.val(self.obj.getNowTimeStr() + '/' + self.obj.getTotalTimeStr() +((self.obj.getNowMSec()==0)?'(バッファ中)':''));
        //self.status.val(self.obj.getNowMSec() + '/' + self.obj.getTotalMSec());
        if(self.tm > self.obj.getNowMSec()){
            clearInterval(self.timer);
            self.timer = null;
            self.tm = 0;
            self.obj.stop();
            self.status.val('-');
            self.stopBtn.attr('disabled',true);
        } else {
            self.tm = self.obj.getNowMSec();
        };
    };
    this.timer = setInterval(timerFunc,100);
};
MML.prototype.unsetTimer = function(){
    if(this.timer != null){
        clearInterval(this.timer);
        this.timer = null;
    }
};
MML.prototype.setButtonFunc = function(){
    var self = this;
    this.playBtn.bind('click',function(){
        if(self.obj.isPlaying()){
            self.obj.pause();
        } else if(self.obj.isPaused()) {
            self.obj.play();
            self.setTimer();
        } else {
            self.obj.play(self.textArea.val());
            self.stopBtn.attr('disabled',false);
            self.setTimer();
        }
    }).attr('disabled',false);
    this.stopBtn.bind('click',function(){
        self.unsetTimer();
        self.obj.stop();
        self.status.val('-');
        $(this).attr('disabled',true);
    }).attr('disabled',true);
};



$(function(){
    var mml;
    JSMML.onLoad=function(){
        mml = new MML(
            $('#play'),
            $('#stop'),
            $('#source'),
            $('#status')
        );
    };

});