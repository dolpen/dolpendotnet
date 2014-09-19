JSMML = (function() {
    return function(a) {
        this.mmlPlayer = document.getElementById(JSMML.mmlID)
        this.initialize.call(this)
    }
})();


JSMML.meta = {
    '@prefix' : '<http://purl.org/net/ns/doas#>',
    '@about' : '<JSMML.js>',
    a : ':JavaScript',
    title : 'FLMML for JavaScript',
    created : '2007-10-06',
    release : {revision : '1.2.3', created : '2010-06-15'},
    contributor : {name : 'Yuichi Tateno', homepage : '<http://rails2u.com/>'},
    author : {name : 'Logue', homepage : '<http://logue.be/>'},
    license : '<http://www.opensource.org/licenses/mit-license.php>',
    dependencies : 'JSMML.swf, jQuery, SWFObjects'
};

JSMML.VESION = JSMML.meta.release.revision;

JSMML.setSWFVersion = function(v) {
    JSMML.SWF_VERSION = v;
};

JSMML.SWF_VERSION = 'JSMMLはロードされていません.';
JSMML.toString = function() {return 'JSMML VERSION: ' + JSMML.VESION + ', SWF_VERSION: ' + JSMML.SWF_VERSION};
JSMML.swfurl = '/static/swf/jsmml.swf';
JSMML.mmlID = 'jsmml';
JSMML.containerID = JSMML.mmlID + '_container';
JSMML.onLoad = function() {};
JSMML.loaded = false;
JSMML.instances = {};
JSMML.notRunning = 'JSMMLはswfファイルの読み込みにHTTPを利用しなければなりません。';
JSMML.init = function(a) {
    var b = {swfLiveConnect : true, bgcolor : '#FFFFFF', quality : 'high', allowScriptAccess : 'always', style : 'display:inline;'};
    var f = (a ? a : JSMML.swfurl) + '?' + (new Date()).getTime();
    if($('#'+JSMML.containerID).size()<1){
        $('body').append($('<div />').attr('id', JSMML.containerID));
    }
    if(!document.location.protocol.match(/http/i)){
        $('#'+JSMML.containerID).text(JSMML.notRunning);
    } else {
        try {
            swfobject.embedSWF(f, JSMML.containerID, 1, 1, '10.0.0', 'expressInstall.swf', '',
                {swfLiveConnect : true, bgcolor : '#FFFFFF', quality : 'high', allowScriptAccess : 'always', style : 'display:inline;'},
                {id : JSMML.mmlID}
            );
        } catch (c) {
            $('#'+JSMML.containerID).text(c);
        }
    }
};
JSMML.initASFinish = function() {
    JSMML.loaded = true;
    JSMML.onLoad()
};
JSMML.eventInit = function() {
    JSMML.init()
};
JSMML.prototype = {
    initialize : function() {
        this.onFinish = function() {};
        this.pauseNow = false;
    },
    uNum : function() {
        if (!this._uNum) {
            this._uNum = this.mmlPlayer._create();
            JSMML.instances[this._uNum] = this
        }
        return this._uNum
    },
    play : function(a) {
        if (!a && this.pauseNow) {
            this.mmlPlayer._play(this.uNum())
        } else {
            if (a) {
                this.score = a
            }
            this.mmlPlayer._play(this.uNum(), this.score)
        }
        this.pauseNow = false
    },
    stop : function() {
        this.mmlPlayer._stop(this.uNum())
    },
    pause : function() {
        this.pauseNow = true;
        this.mmlPlayer._pause(this.uNum())
    },
    destroy : function() {
        this.mmlPlayer._destroy(this.uNum());
        delete JSMML.instances[this.uNum()]
    },
    isPlaying : function() {
        return this.mmlPlayer._isPlaying(this.uNum())
    },
    isPaused : function() {
        return this.mmlPlayer._isPaused(this.uNum())
    },
    setMasterVolume : function(a) {
        return this.mmlPlayer._setMasterVolume(this.uNum(), a)
    },
    getWarnings : function() {
        return this.mmlPlayer._getWarnings(this.uNum())
    },
    getTotalMSec : function() {
        return this.mmlPlayer._getTotalMSec(this.uNum())
    },
    getTotalTimeStr : function() {
        return this.mmlPlayer._getTotalTimeStr(this.uNum())
    },
    getNowMSec : function() {
        return this.mmlPlayer._getNowMSec(this.uNum())
    },
    getNowTimeStr : function() {
        return this.mmlPlayer._getNowTimeStr(this.uNum())
    }
};
jQuery(document).ready(JSMML.eventInit);
