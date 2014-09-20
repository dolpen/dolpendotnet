package net.dolpen.gae.libs.servlet;

import net.dolpen.gae.libs.jpa.JPA;

import javax.servlet.http.*;

public class Forwards {

    private static final String JSP_PREFIX = "/WEB-INF/view";

    private static final String ERROR_PATH = "/error/503.jsp";

    /**
     * JSPにフォワードします
     *
     * @param target 遷移先
     * @param req    リクエスト
     * @param resp   レスポンス
     */
    public static void forward(String target, HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher(JSP_PREFIX + target).forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                req.getRequestDispatcher(JSP_PREFIX + ERROR_PATH).forward(req, resp);
            } catch (Exception e1) {
            }
        }
    }

    /**
     * サーバーエラー
     *
     * @param code エラーコード
     * @param req  リクエスト
     * @param resp レスポンス
     */
    public static void forwardError(int code, HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher(JSP_PREFIX + "/error/" + code + ".jsp").forward(req, resp);
        } catch (Exception e) {
        }
    }

    /**
     * 別URLにリダイレクトします
     *
     * @param target 遷移先
     * @param resp   レスポンス
     */
    public static void redirect(String target, HttpServletResponse resp) {
        if(JPA.em()!=null&&JPA.em().isOpen())JPA.em().close();
        try {
            resp.sendRedirect(target);
        } catch (Exception e) {
        }
    }
}