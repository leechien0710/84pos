import { HTMLAttributes, FC } from "react";
import { Typography } from "@mui/material";
import { HeaderLanding } from "../../common/HeaderLanding";
import { useStyles } from "./Policy.style";

export const Policy: FC<HTMLAttributes<HTMLDivElement>> = () => {
  const classes = useStyles();

  return (
    <div>
      <HeaderLanding />
      <div className={classes.content}>
        <div className={classes.wrapper}>
          <Typography className={classes.policyTitle}>
            CHÍNH SÁCH BẢO VỆ DỮ LIỆU CÁ NHÂN
          </Typography>
          <div>
            <Typography className={classes.title}>
              1. Chính sách bảo mật thông tin
            </Typography>
            <Typography className={classes.desc}>
              Bảo mật thông tin của Qúy Khách hàng là việc làm cần thiết và quan
              trọng đối với 84Pos. 84Pos cam kết bảo mật các dữ liệu của Qúy
              Khách trên tài khoản sử dụng Phần mềm 84Pos, trừ các trường hợp:
            </Typography>
            <ul className={classes.item}>
              <li>
                Thông tin, dữ liệu của Qúy Khách được công khai trên tên miền,
                website, fanpage của Qúy Khách và trên các phương tiện thông tin
                đại chúng công cộng.
              </li>
              <li>
                Theo yêu cầu bằng văn bản của Qúy Khách và/hoặc Quý Khách đồng
                ý, yêu cầu sử dụng các dịch vụ, tiện ích tích hợp trên hệ thống
                phần mềm.
              </li>
              <li>
                Theo yêu cầu của cơ quan nhà nước có thẩm quyền và quy định pháp
                luật.
                <br />
                Các trường hợp bất khả kháng.
              </li>
            </ul>
            <Typography className={classes.desc}>
              Khách hàng truy cập, đăng ký, sử dụng 84Pos có ý nghĩa rằng khách
              hàng đã đồng ý và chấp nhận các quy định trong chính sách bảo mật
              của chúng tôi. Khách hàng sẽ tự chịu trách nhiệm về bảo mật và lưu
              giữ mọi hoạt động sử dụng dịch vụ dưới tên đăng ký, mật khẩu và
              hộp thư điện tử và/hoặc số điện thoại của mình. 84Pos không chịu
              trách nhiệm về các thất thoát dữ liệu, bí mật thông tin của khách
              hàng do khách hàng vô tình hoặc cố ý gây ra. Ngoài ra, Khách hàng
              có trách nhiệm thông báo kịp thời cho chúng tôi về những hành vi
              sử dụng trái phép, lạm dụng, vi phạm bảo mật, lưu giữ tên đăng ký
              và mật khẩu của bên thứ ba để có biện pháp giải quyết phù hợp.
            </Typography>
            <Typography className={classes.title}>
              2. Những thông tin 84Pos thu thập
            </Typography>
            <Typography className={classes.desc}>
              Để có thể sử dụng đầy đủ các tiện ích có trên sản phẩm, dịch vụ
              84Pos cung cấp, khách hàng cần phải đăng ký dùng và cung cấp các
              thông tin cá nhân của mình. Các thông tin 84Pos cần khách hàng
              cung cấp và/hoặc các thông tin 84Pos tự động lưu trữ là các thông
              tin cơ bản bao gồm:
            </Typography>
            <Typography>
              <strong>a, Thông tin cá nhân</strong>
            </Typography>
            <ul className={classes.item}>
              <li>Tên công ty, cửa hàng, địa chỉ kinh doanh</li>
              <li>Họ tên, địa chỉ cư trú</li>
              <li>Email, số điện thoại di động</li>
              <li>
                Địa chỉ IP (Internet Protocol), loại trình duyệt web (Browser),
                tốc độ đường truyền, số trang khách hàng xem, thời gian khách
                hàng viếng thăm, những địa chỉ mà Browser này truy xuất đến.
              </li>
              <li>
                Để tăng tính trải nghiệm người dùng, chúng tôi có sử dụng các
                dịch vụ khác để thu thập các thông tin như địa chỉ IP, tên thiết
                bị, hệ điều hành thiết bị, loại thiết bị, ID thiết bị, Số lượt
                nhấn nút đăng nhập, số lượt sử dụng tính năng trong ứng dụng.
              </li>
              <li>
                Dữ liệu về Nhật ký sự cố và dữ liệu chẩn đoán, giúp chúng tôi
                cải thiện ứng dụng của mình tốt hơn
              </li>
            </ul>
            <Typography className={classes.desc}>
              Dữ liệu của khách hàng được 84Pos bảo vệ trong Chính sách bảo mật
              này là tất cả các dữ liệu trên phần mềm 84Pos được khởi tạo
              và/hoặc có được trong suốt thời gian khách hàng sử dụng sản phẩm,
              dịch vụ của 84Pos.
            </Typography>
            <Typography className={classes.desc}>
              Tất cả các truy cập này chỉ được chúng tôi thực hiện khi có sự
              đồng ý của bạn. Bạn hiểu và chấp nhận rằng, khi bạn đã cấp quyền
              cho chúng tôi, bạn sẽ không có bất kỳ khiếu nại nào đối với việc
              truy cập này.
            </Typography>
            <Typography>
              <strong>
                b, Khi Khách hàng liên kết tài khoản Facebook để sử dụng dịch
                vụ, 84Pos sẽ yêu cầu các quyền sau:
              </strong>
            </Typography>
            <ul className={classes.item}>
              <li>
                Quyền publish_profile: Cho phép ứng dụng 84Pos đọc các trường hồ
                sơ công khai trên Tài khoản cá nhân của người dùng.
              </li>
              <li>
                Quyền user_videos: Cho phép ứng dụng 84Pos đọc danh sách video
                do Người dùng tải lên
              </li>
              <li>
                Quyền user_posts: Cho phép người dùng ứng dụng 84Pos tạo và chia
                sẻ bài viết từ dòng thời gian của họ trên Facebook.
              </li>
              <li>
                Quyền pages_manage_metadata: Cho phép ứng dụng 84Pos đăng ký và
                nhập webhook về hoạt động trên trang cũng như cập nhật cài đặt
                trên trang.
              </li>
              <li>
                Quyền pages_messaging: Cho phép ứng dụng 84Pos quản lý và truy
                cập các cuộc trò chuyện trên Trang trong Messenger.
              </li>
              <li>
                Quyền business Asset User Profile Access: cho phép ứng dụng
                84Pos xem được người dùng tương tác với nội dung doanh nghiệp
                của bạn, chẳng hạn như id, ids_for_business, tên và ảnh.
              </li>
              <li>
                Quyền publish_pages: Cho phép 84Pos trả lời bình luận trên
                Trang.
              </li>
              <li>
                Quyền read_page_mailboxes, pages_messaging: Cho phép ứng dụng
                84Pos đọc, nhận tin nhắn của Trang.
              </li>
              <li>
                Quyền ads_read: Cho phép ứng dụng 84Pos xem và quản lý quảng cáo
                trên Facebook.
              </li>
              <li>
                Quyền live Video API: Cho phép ứng dụng 84Pos quản lý các video
                trực tiếp trên các dòng thời gian của Trang.
              </li>
            </ul>
            <Typography className={classes.title}>
              3. Phạm vi sử dụng thông tin
            </Typography>
            <Typography>Các thông tin 84Pos thu thập nhằm:</Typography>
            <ul className={classes.item}>
              <li>Cung cấp thông tin, các dịch vụ đến khách hàng.</li>
              <li>Hỗ trợ và giải đáp thắc mắc của khách hàng.</li>
              <li>
                Cung cấp cho khách hàng thông tin về các phiên bản phần mềm mới
                nhất &amp; các bản cập nhật trên sản phẩm của chúng tôi.
              </li>
              <li>
                Thực hiện các hoạt động quảng bá liên quan đến các sản phẩm và
                dịch vụ của chúng tôi.
              </li>
              <li>Đo lường và cải thiện các dịch vụ của chúng tôi.</li>
              <li>
                Liên lạc và giải quyết với khách hàng trong những trường hợp đặc
                biệt.
              </li>
              <li>
                Chia sẻ thông tin cần thiết cho bên đối tác nếu nhận được sự
                đồng ý của Khách hàng.
              </li>
              <li>
                Các mục đích khác được phép theo quy định của pháp luật và chúng
                tôi sẽ có thông báo cho khách hàng trong từng trường hợp cụ thể.
              </li>
            </ul>
            <Typography className={classes.title}>
              4. Thời gian lưu trữ trên 84Pos
            </Typography>
            <Typography className={classes.desc}>
              Dữ liệu cá nhân của khách hàng sẽ được lưu trữ cho đến khi có yêu
              cầu hủy bỏ hoặc tự khách hàng đăng nhập và thực hiện hủy bỏ. Còn
              lại, trong mọi trường hợp thông tin cá nhân khách hàng sẽ được bảo
              mật trên hệ thống máy chủ của 84Pos
            </Typography>
            <Typography className={classes.title}>
              5. ĐỊA CHỈ CỦA ĐƠN VỊ THU THẬP VÀ QUẢN LÝ THÔNG TIN
            </Typography>
            <Typography className={classes.desc}>
              Địa chỉ: Quảng Nguyên, Quảng Phú Cầu, Ứng Hòa, Hà Nội.
            </Typography>
            <Typography className={classes.desc}>
              Hotline: (+84) 0379452201.
            </Typography>
            <Typography className={classes.title}>
              6. PHƯƠNG THỨC VÀ CÔNG CỤ ĐỂ NGƯỜI DÙNG TIẾP CẬN, CHÍNH SỬA VÀ XÓA
              DỮ LIỆU CÁ NHÂN
            </Typography>
            <Typography className={classes.desc}>
              Khách hàng có quyền tự kiểm tra, cập nhật, điều chỉnh, xóa thông
              tin cá nhân của mình bằng cách đăng nhập vào tài khoản và chỉnh
              sửa thông tin cá nhân hoặc yêu cầu 84Pos thực hiện việc này.
            </Typography>
            <Typography className={classes.title}>
              7. CAM KẾT BẢO MẬT THÔNG TIN KHÁCH HÀNG
            </Typography>
            <Typography>
              Thông tin của Khách hàng trên 84Pos sẽ được 84Pos cam kết bảo mật
              tuyệt đối theo chính sách bảo vệ thông tin cá nhân của 84Pos. Việc
              thu thập và sử dụng thông tin của mỗi Khách hàng chỉ được thực
              hiện khi có sự đồng ý của Khách hàng đó, trừ những trường hợp pháp
              luật có quy định khác. 84Pos cam kết:
            </Typography>
            <ul className={classes.item}>
              <li>
                Không sử dụng, không chuyển giao, cung cấp hay tiết lộ cho bên
                thứ ba nào về thông tin cá nhân của Khách hàng khi không có sự
                cho phép hoặc đồng ý từ Khách hàng, trừ những trường hợp pháp
                luật có quy định khác.
              </li>
              <li>
                Bảo mật tuyệt đối mọi thông tin giao dịch trực tuyến của Khách
                hàng bao gồm thông tin hóa đơn, chứng từ kế toán số hóa tại khu
                vực dữ liệu trung tâm an toàn của 84Pos.
              </li>
            </ul>
            <Typography className={classes.desc}>
              Chính sách bảo mật thông tin của 84Pos đảm bảo rằng chúng tôi thu
              thập, sử dụng và bảo vệ thông tin cá nhân của người dùng một cách
              an toàn và đáng tin cậy. Chúng tôi tuân thủ các quy định pháp luật
              về bảo mật thông tin cá nhân và cam kết nâng cao các biện pháp bảo
              mật để bảo vệ thông tin của người dùng.
            </Typography>
            <Typography className={classes.title}>
              8. LIÊN HỆ CHÚNG TÔI
            </Typography>
            <Typography>
              Nếu khách hàng có bất kỳ câu hỏi, yêu cầu hoặc phàn nàn liên quan
              đến chính sách bảo mật thông tin, có thể liên hệ với chúng tôi
              thông qua các kênh liên lạc bên dưới. Chúng tôi đảm bảo trả lời và
              giải quyết toàn bộ vấn đề một cách nhanh chóng và hiệu quả.
            </Typography>
            <ul className={classes.item}>
              <li>Website: https://84pos.vn/</li>
              <li>Fanpage: https://www.facebook.com/84pos.vn/</li>
              <li>Hotline: (+84) 0379452201</li>
              <li>Địa chỉ: Quảng Nguyên, Quảng Phú Cầu, Ứng Hòa, Hà Nội</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};
