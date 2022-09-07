/*
  element 생성 함수
  element이름(name),속성(attributes),자식노드를 포함하는 element를 만들어 반환한다.

  속성(attributes)객체 구조 { 속성명1:속성값1, 속성명2:속성값2,....}
*/

export default function makeElements(name, attributes, ...contents){
  const node = document.createElement(name);
  if(attributes){
    for(let attr in attributes) {
      if(attributes.hasOwnProperty(attr)){
        node.setAttribute(attr, attributes[attr]);
      }
    } 
  }

  // 컨텐츠가 있는경우
  if(contents){
    for(let ele of contents){
      let child = ele;
      //문자열이면 텍스트노드생성
      if( typeof child == 'string') {
        child =document.createTextNode(child);
      }
      node.appendChild(child);
    }
  }

  return node;
}
