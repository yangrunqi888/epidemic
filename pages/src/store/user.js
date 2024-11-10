export default{
    state:{
        imgUrl:"",//头像地址
        imageLoading:false,
    },
    mutations:{
        //更新头像url
        updateImageURL(state,val){
            state.imageLoading=false;
            state.imgUrl=val;
        },
        setImageLoading(state,val){
            state.imageLoading=val;
        }
    }
}