describe('Home Page Test', () => {
    it('Should load the Home page correctly', () => {
        // 访问本地前端页面
        cy.visit('http://localhost:3000');

        // 检查页面是否包含 "Star Spot" 文本
        cy.contains('Star Spot').should('be.visible');

        // 测试 "Recommended Sites" 按钮是否存在
        cy.contains('Recommended Sites').click();

        // 断言页面跳转后，新的页面包含 "Meteor Shower"
        cy.contains('Meteor Shower').should('be.visible');
    });
});
