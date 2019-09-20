module.exports = {
    db: 'mongodb://admin:admin@ds021356.mlab.com:21356/northwindnode',
    sessionSecret: 'developmentSessionSecret',
    facebook: {
        clientID: '541523029512626',
        clientSecret: '875501c5e622268479129f6d8945e89c',
        callbackURL: 'https://mean-book-app.herokuapp.com/oauth/facebook/callback'
    },
    twitter: {
        clientID: 'gDEWOh8pgaGXFP429kRo30zOM',
        clientSecret: '4I3FLVJLnHSIxmYF3BUsqzFZDNkVJi9sGn2czUwZr2G8d8WILD',
        callbackURL: 'https://mean-book-app.herokuapp.com/oauth/twitter/callback'
    },
    google: {
        clientID: '818773428299-4c7l7a7bdg5hjc8hjrk76othvndjq3sj.apps.googleusercontent.com',
        clientSecret: 'ZWPl-iSDndTU33dTVNz-rZnU',
        callbackURL: 'https://mean-book-app.herokuapp.com/oauth/google/callback'
    }
};