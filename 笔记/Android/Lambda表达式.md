##1.Lambda表达式
    //一般形式
    Observable.create(new ObservableOnSubscribe<Identity>() {

        @Override
        public void subscribe(ObservableEmitter<Identity> e) throws Exception {

        }
    }).subscribe(new SimpleObserver<Identity>() {
        @Override
        public void onNext(Identity identity) {

        }
    });

    //Lambda形式，(ObservableOnSubscribe<Identity>)其实是对匿名内部类ObservableOnSubscribe的一个强转，即
    e->后面部分的强转
    Observable.create((ObservableOnSubscribe<Identity>) e -> {

    }).subscribe(new SimpleObserver<Identity>() {
        @Override
        public void onNext(Identity identity) {

        }
    });