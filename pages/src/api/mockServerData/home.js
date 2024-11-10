import Mock from 'mockjs'
export default
{
    getData:()=>{
        return {
            code:20001,
            data:[
                {
                    sno:"000",
                    sname:"超级赛亚人",
                    age:999,
                    address:"???"
                },
                {
                    sno:"001",
                    sname:"凹凸曼",
                    age:999,
                    address:"???"
                }
            ]
        }
    }
}