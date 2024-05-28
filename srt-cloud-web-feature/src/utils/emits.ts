import mitt, { Emitter } from 'mitt'

export default mitt() as Emitter<any>

// 这段代码使用了 mitt 库创建了一个事件总线，并将其导出为默认的事件发射器。让我们逐行解释它：
// import mitt, { Emitter } from 'mitt'：从 mitt 库中导入 mitt 函数和 Emitter 类型。
//     mitt 函数用于创建一个新的事件总线实例。
//     Emitter 类型表示事件发射器的类型。
// export default mitt() as Emitter<any>：
//     调用 mitt() 函数创建一个新的事件总线实例。
//     使用 as Emitter<any> 将事件总线实例转换为 Emitter<any> 类型。
//     最终将其作为默认导出，以便其他模块可以导入并使用该事件总线实例进行事件的订阅和触发。
// 这段代码的作用是创建一个全局的事件总线，用于在应用程序的不同部分之间进行事件的通信。通过事件总线，不同模块之间可以方便地发送和接收事件，实现解耦和灵活性。
