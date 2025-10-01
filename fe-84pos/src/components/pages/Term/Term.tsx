import { HTMLAttributes, FC } from "react";
import { Typography } from "@mui/material";
import { HeaderLanding } from "../../common/HeaderLanding";
import { useStyles } from "./Term.style";

export const Term: FC<HTMLAttributes<HTMLDivElement>> = () => {
  const classes = useStyles();

  return (
    <div>
      <HeaderLanding />
      <div className={classes.content}>
        <div className={classes.wrapper}>
          <Typography className={classes.policyTitle}>
            ĐIỀU KHOẢN VÀ DỊCH VỤ
          </Typography>
          <div>
            <Typography className={classes.title}>
              1. PHẠM VI ÁP DỤNG
            </Typography>
            <Typography className={classes.desc}>
              Bằng việc đăng ký tài khoản sử dụng các sản phẩm, dịch vụ của
              84Pos (bao gồm cả tài khoản sử dụng thử, sử dụng chính thức; Tài
              khoản dùng bản miễn phí hay bản trả phí; Tài khoản sử dụng ở phiên
              bản web hay phiên bản ứng dụng (app)), Quý Khách biết, đồng ý tuân
              thủ toàn bộ các nội dung được đề cập tại Điều khoản &amp; Điều
              kiện sử dụng này.
              <br />
              Trường hợp có bất kỳ nội dung nào của Điều khoản &amp; Điều kiện
              sử dụng này hết hiệu lực hoặc không thể thi hành vì bất cứ lý do
              gì sẽ chỉ ảnh hưởng đến nội dung đã xác định hết hiệu lực đó và
              không ảnh hưởng đến hiệu lực của các nội dung còn lại. Nếu có sự
              khác biệt giữa Điều khoản &amp; Điều kiện sử dụng này và Hợp đồng,
              văn bản thỏa thuận khác thì quy định nào mới nhất sẽ có hiệu lực,
              trừ trường hợp hai bên có thỏa thuận khác bằng văn bản.
            </Typography>
            <Typography className={classes.title}>
              2. SỬ DỤNG KHU VỰC CHUNG VÀ KHU VỰC RIÊNG
            </Typography>
            <Typography className={classes.desc}>
              a) Sử dụng khu vực chung:
              <br />
              Quý Khách hiểu và chấp nhận rằng Quý Khách có quyền khai thác và
              sử dụng Khu vực chung trên Phần mềm 84Pos theo quyết định và xem
              xét duy nhất của 84Pos, mọi yêu cầu của Qúy Khách về sử dụng Khu
              vực chung trên Phần mềm 84Pos cho mục đích riêng của Qúy Khách có
              thể được tính phí ngoài phí dịch vụ đã thông báo ban đầu.
              <br /> b) Sử dụng khu vực riêng:
              <br />
              Quý Khách có toàn quyền sử dụng Khu vực riêng được bảo vệ bằng mật
              khẩu cho các hoạt động của mình. 84Pos không can thiệp và không
              chịu trách nhiệm về các thao tác của Qúy Khách tác động lên dữ
              liệu và thông tin trên Khu vực riêng. Mọi yêu cầu phát sinh trong
              việc sử dụng Khu vực riêng không nằm trong cam kết ban đầu có thể
              được tính phí.
            </Typography>
            <ul className={classes.list}>
              <li className={classes.item}>
                Quý Khách yêu cầu nhằm mục đích hỗ trợ sử dụng, xử lý sự cố
              </li>
              <li className={classes.item}>
                Khi có yêu cầu của cơ quan nhà nước, Tòa án… theo quy định của
                pháp luật
              </li>
              <li className={classes.item}>
                Trong tình huống khẩn cấp, nhằm ngăn chặn xâm nhập trái phép
                hoặc tấn công phá hoại từ bên ngoài.
              </li>
            </ul>
            <Typography className={classes.desc}>
              Quý Khách hiểu và chấp nhận rằng Quý Khách không có quyền yêu cầu
              truy cập khu vực riêng của 84Pos, khu vực riêng của cá nhân, tổ
              chức khác, khu vực quản trị Dịch vụ, Hệ thống và Quản trị website,
              ứng dụng.
            </Typography>
            <Typography className={classes.title}>
              3. SỬ DỤNG HỢP PHÁP
            </Typography>
            <Typography className={classes.desc}>
              Quý Khách cam kết rằng Quý Khách sử dụng các Sản phẩm, dịch vụ của
              84Pos vào các hoạt động hợp pháp, phù hợp với thuần phong mỹ tục
              của Việt Nam, không gây ảnh hưởng đến quyền lợi của bất kì cơ
              quan, tổ chức, cá nhân nào.
              <br />
              84Pos có quyền tạm ngừng hoặc ngừng cung cấp Sản phẩm, dịch vụ cho
              Quý Khách mà không hoàn lại bất kỳ một chi phí nào khi Quý Khách
              thực hiện một hoặc một số trường hợp sau:
            </Typography>
            <ol className={classes.list}>
              <li className={classes.item}>
                Quý Khách sử dụng Sản phẩm, dịch vụ của 84Pos để kinh doanh các
                loại hàng hóa, dịch vụ bị cấm kinh doanh theo quy định của pháp
                luật hoặc sử dụng phần mềm vào bất kỳ mục đích/hình thức nào vi
                phạm pháp luật;
              </li>
              <li className={classes.item}>
                Quý Khách gửi, tạo liên kết hoặc trung chuyển các dữ liệu mang
                tính chất bất hợp pháp, đe doạ, lừa dối, thù hằn, xuyên tạc, nói
                xấu, tục tĩu, khiêu dâm, xúc phạm… hay các hình thức khác bị
                pháp luật ngăn cấm dưới bất kỳ cách thức nào;
              </li>
              <li className={classes.item}>
                Quý Khách lưu trữ, truyền bá các dữ liệu cấu thành hoặc khuyến
                khích các hình thức phạm tội; hoặc các dữ liệu mang tính vi phạm
                pháp luật về quyền sở hữu trí tuệ hoặc bất kỳ vi phạm nào làm
                ảnh hưởng đến quyền của cá nhân, tổ chức khác được pháp luật
                công nhận, hoặc vi phạm bất kỳ các quy định nào của pháp luật;
              </li>
              <li className={classes.item}>
                Quý Khách sử dụng các chương trình có khả năng làm tắc nghẽn
                hoặc đình trệ hệ thống của 84Pos như gây cạn kiệt tài nguyên hệ
                thống, làm quá tải bộ vi xử lý và bộ nhớ;
              </li>
              <li className={classes.item}>
                Quý Khách sử dụng Sản phẩm, dịch vụ của 84Pos để xâm nhập trái
                phép, phá hoại website, phần mềm khác hoặc gây ảnh hưởng đến
                khách hàng khác của 84Pos, hoặc Bên thứ ba.
              </li>
            </ol>
            <Typography className={classes.title}>
              5. CHÍNH SÁCH BẢO MẬT THÔNG TIN
            </Typography>
            <Typography className={classes.desc}>
              Bảo mật thông tin của Qúy Khách hàng là việc làm cần thiết và quan
              trọng đối với 84Pos. 84Pos cam kết bảo mật các dữ liệu của Qúy
              Khách trên tài khoản sử dụng Phần mềm 84Pos, trừ các trường hợp:
            </Typography>
            <Typography className={`${classes.desc} ${classes.list}`}>
              - Thông tin, dữ liệu của Quý Khách được công khai trên tên miền,
              website, fanpage của Quý Khách và trên các phương tiện thông tin
              đại chúng công cộng;
              <br />
              - Theo yêu cầu bằng văn bản của Quý Khách và/hoặc Quý Khách đồng
              ý, yêu cầu sử dụng các dịch vụ, tiện ích tích hợp trên hệ thống
              phần mềm;
              <br />
              - Theo yêu cầu của cơ quan nhà nước có thẩm quyền và quy định pháp
              luật;
              <br />- Các trường hợp bất khả kháng.
            </Typography>
            <Typography className={classes.title}>6. TRÁCH NHIỆM</Typography>
            <Typography className={classes.desc}>
              84Pos không chịu trách nhiệm trong các trường hợp sau:
            </Typography>
            <ol className={classes.list}>
              <li className={classes.item}>
                84Pos không chịu trách nhiệm về việc Qúy Khách sử dụng phần mềm
                của 84Pos để phát tán các thông tin, dữ liệu xâm phạm đến quyền
                lợi của cá nhân, tổ chức khác.
              </li>
              <li className={classes.item}>
                Qúy Khách phải tự chịu trách nhiệm về các hành vi xâm phạm đến
                quyền lợi của cá nhân, tổ chức khác khi sử dụng các Sản phẩm,
                dịch vụ của 84Pos.
              </li>
              <li className={classes.item}>
                84Pos không chịu trách nhiệm về việc chậm trễ trong quá trình
                truyền, nhận dữ liệu vì những nguyên nhân không thể kiểm soát
                như sự cố kỹ thuật, thiên tai, chiến tranh, bạo loạn, hỏa hoạn,
                đình công hoặc các biến cố không lường trước khác.
              </li>
              <li className={classes.item}>
                84Pos không chịu trách nhiệm trong trường hợp Qúy Khách sử dụng
                thông tin sai lệch, không đầy đủ hoặc không có quyền sở hữu hợp
                pháp đối với các tài khoản, dịch vụ, nội dung liên quan đến phần
                mềm.
              </li>
            </ol>
            <Typography className={classes.title}>
              7. ĐIỀU KHOẢN CHUNG
            </Typography>
            <Typography className={classes.desc}>
              a) 84Pos có quyền sửa đổi, bổ sung hoặc thay thế các nội dung
              trong Điều khoản &amp; Điều kiện này bất cứ lúc nào. Các nội dung
              sửa đổi, bổ sung hoặc thay thế sẽ được thông báo đến Qúy Khách
              trên phần mềm hoặc các kênh thông tin khác mà 84Pos cho là phù hợp
              và sẽ có hiệu lực ngay khi được thông báo.
            </Typography>
            <ul className={classes.list}>
              <li className={classes.item}>
                Các quy định về việc sửa đổi, bổ sung, thay thế hoặc hủy bỏ bất
                kỳ nội dung nào của Điều khoản &amp; Điều kiện này sẽ không cần
                sự đồng ý của Qúy Khách.
              </li>
              <li className={classes.item}>
                Các điều khoản trong bản Điều khoản &amp; Điều kiện sử dụng này
                sẽ được điều chỉnh và tuân thủ theo các quy định của pháp luật
                Việt Nam.
              </li>
              <li className={classes.item}>
                Qúy Khách hiểu và đồng ý rằng việc tiếp tục sử dụng phần mềm sau
                khi có sửa đổi, bổ sung, thay thế nội dung của Điều khoản &amp;
                Điều kiện này được xem như là sự đồng ý và chấp nhận các nội
                dung đã được thay đổi.
              </li>
            </ul>
            <Typography className={classes.title}>
              8. QUYỀN SỞ HỮU TRÍ TUỆ
            </Typography>
            <Typography className={classes.desc}>
              Toàn bộ nội dung, hình ảnh, và các thông tin khác có trong phần
              mềm 84Pos đều thuộc quyền sở hữu trí tuệ của 84Pos hoặc các đối
              tác của 84Pos. Việc sao chép, sử dụng hoặc phân phối các thông tin
              này mà không có sự đồng ý trước bằng văn bản của 84Pos là vi phạm
              pháp luật.
            </Typography>
            <Typography className={classes.desc}>
              Quý Khách đồng ý không sao chép, chỉnh sửa, phân phối hoặc tạo ra
              các sản phẩm phái sinh từ các nội dung có trong phần mềm 84Pos,
              trừ khi có sự cho phép rõ ràng bằng văn bản từ 84Pos.
            </Typography>
            <Typography className={classes.title}>9. KẾT THÚC</Typography>
            <Typography className={classes.desc}>
              84Pos có quyền tạm ngừng hoặc ngừng cung cấp các Sản phẩm, dịch vụ
              của mình cho Qúy Khách trong các trường hợp sau:
            </Typography>
            <ul className={classes.list}>
              <li className={classes.item}>
                Quý Khách vi phạm các Điều khoản &amp; Điều kiện này hoặc vi
                phạm pháp luật;
              </li>
              <li className={classes.item}>
                Theo yêu cầu của các cơ quan nhà nước có thẩm quyền;
              </li>
              <li className={classes.item}>
                Quý Khách sử dụng phần mềm vào các mục đích không hợp pháp hoặc
                gây tổn hại đến quyền lợi của bên thứ ba.
              </li>
            </ul>
            <Typography className={classes.desc}>
              Trong trường hợp tạm ngừng hoặc chấm dứt cung cấp dịch vụ, 84Pos
              không có nghĩa vụ hoàn trả các khoản phí dịch vụ mà Qúy Khách đã
              thanh toán trước đó.
            </Typography>
            <Typography className={classes.desc}>
              Bản Điều khoản &amp; Điều kiện này có hiệu lực từ ngày ký kết cho
              đến khi chấm dứt hoặc thay thế bằng các quy định mới.
            </Typography>
          </div>
        </div>
      </div>
    </div>
  );
};
