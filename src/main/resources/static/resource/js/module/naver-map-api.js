export default class MapUtile {

    constructor(map) {
        this.map = map;
        this.markers = [];
        this.infowindows = [];
    }

    CustomOverlay = function(options) {
        this._element = $('<div style="position:absolute;left:0;top:0;width:120px;height:30px;line-height:30px;text-align:center;background-color:#fff;border:2px solid #f00;">커스텀오버레이</div>')
        this.setPosition(options.position);
        this.setMap(options.map || null);
    };

    //마커들 생성 + 위치이동
    makeMarkers(data) {

        //맵 초기상태 변경
        this.defaultSet();

        const bound = new naver.maps.LatLngBounds();
        this.beforeBound = bound;

        //마커 생성
        data.forEach((ele,idx) => {
            const marker =
                this.makeMarker(
                    ele.fclat,
                    ele.fclng,
                    `
                                <div class="facility-location-marker marker">
                                    <span onclick="location.href='/facilities/${ele.fcno}'" class="marker-info">${ele.fcname}</span>
                                    <i class="fa-solid fa-location-dot"></i>
                                </div>`,
                    idx + 1
                )

            this.markers.push(marker);
            bound.extend(new naver.maps.LatLng(ele.fclat, ele.fclng));

            //마커 클릭 이벤트
            naver.maps.Event.addListener(marker, 'dblclick', () => {
                this.moveMap(ele.fclat, ele.fclng);

            });

        });
        this.map.panToBounds(bound)

    }

    //내 위치 마커생성 + 위치이동
    makeMyMarker(lat,lng) {
        if(!this.myMarker) {
            const myMarker =
                this.makeMarker(
                    lat,
                    lng,
                    `<div class="my-location-marker marker">
                            <i class="fa-solid fa-house"></i>
                        </div>`,
                    11
                )
            this.myMarker = myMarker;
        }

        if(this.beforeBound) {
            this.beforeBound.extend(new naver.maps.LatLng(lat, lng));
            this.map.panToBounds(this.beforeBound)
        } else {
            this.moveMap(lat,lng);
        }
    }

    //맵 이동
    moveMap(lat,lng) {
        this.map.updateBy(new naver.maps.LatLng(lat, lng),18);
    }

    //마커 생성
    makeMarker(lat,lng,contentHTML,idx) {

        const location = new naver.maps.LatLng(lat,lng);
        const marker = new naver.maps.Marker({
                map: this.map,
                position: location,
                icon: {
                    content : contentHTML,
                    size: new naver.maps.Size(300, 40),
                    anchor: new naver.maps.Point(150, 40),
                },
                zIndex: idx
            });

        return marker;


    }

    //기본 상태
    defaultSet() {

        //내위치 초기화
        if(this.myMarker) {
            this.myMarker.setMap(null);
            this.myMarker = null;
        }

        //기존 마커들 초기화
        if(this.markers.length != 0) {
            this.markers.forEach(ele => {
                ele.setMap(null);
            })
        }

        this.infowindows = [];
        this.markers = [];

        this.map.setZoom(8, true);
    }

}




