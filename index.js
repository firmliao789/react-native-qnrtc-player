import React from 'react'
import {View} from 'react-native'
import {requireNativeComponent} from 'react-native';

const QNRTPlayer = requireNativeComponent('QNRTPlayer');


export default function (props = {style: {width: 300, height: 300}}) {
  return <View style={{width: props.style.width, height: props.style.height, overflow: "hidden"}}>
    <QNRTPlayer {...props}/>
  </View>
}
