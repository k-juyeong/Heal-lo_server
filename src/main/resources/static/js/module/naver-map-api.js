export default class MapUtile {

    constructor(map) {
        this.map = map;
        this.markers = [];
        this.infowindow = new naver.maps.InfoWindow();
    }

    //마커들 생성 + 위치이동
    makeMarkers(data) {
        
        //기존 마커들 초기화
        if(this.markers.length != 0) {
            this.markers.forEach(ele => {
                ele.setMap(null);
            })
        }
        this.markers = [];
        const bound = new naver.maps.LatLngBounds();
        this.beforeBound = bound;

        //마커 생성
        data.forEach(ele => {
            const marker =
                this.makeMarker(
                    ele.fclat,
                    ele.fclng,
                    `<div class="facility-location-marker marker">
                                    <i class="fa-solid fa-location-dot"></i>
                                </div>`
                )

            this.markers.push(marker);
            bound.extend(new naver.maps.LatLng(ele.fclat, ele.fclng));

            //마커 클릭 이벤트
            naver.maps.Event.addListener(marker, 'click', () => {
                const contentHTML = this.createInfoHTML(ele);
                this.infowindow.setContent(contentHTML);

                this.infowindow.open(this.map, marker);

            });

            naver.maps.Event.addListener(marker, 'dblclick', () => {
                this.moveMap(ele.fclat, ele.fclng);

            });

            //지도 클릭 시 인포창 닫기
            naver.maps.Event.addListener(this.map,'click',() => {
                this.infowindow.close();
            },true)

        });
        this.map.panToBounds(bound)

    }

    //내 위치 마커생성 + 위치이동
    makeMyMarker(lat,lng) {
        this.makeMarker(
            lat,
            lng,
            `<div class="my-location-marker marker">
                            <i class="fa-solid fa-house"></i>
                        </div>`
        )

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
    makeMarker(lat,lng,contentHTML) {
        const location = new naver.maps.LatLng(lat,lng);
        const markerOptions = {
            map: this.map,
            position: location,
            icon: {
                content : contentHTML,
                anchor : new naver.maps.Point(10, 0)
            }
        }
        const marker = new naver.maps.Marker(markerOptions);

        return marker;
    }

    //인포창 컨텐츠 생성
    createInfoHTML(data) {
        const contentHTML =
            `
            <div class="infoWindow">
                <p>${data.fcname}</p>
            </div>
            
            `
        return contentHTML;
    }

}




