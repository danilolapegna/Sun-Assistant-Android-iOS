import SwiftUI
import shared

struct BaseCardView: View {
    var message: String
    var sectionTitle: String? = nil
    
    var body: some View {
        VStack(spacing: 8) {
            if let sectionTitle = sectionTitle {
                Text(sectionTitle)
                    .font(.system(size: 18, weight: .semibold))
                    .foregroundColor(.black)
                    .padding(.top, CommonDimen.shared.default_padding)
            }
            let topPadding = sectionTitle == nil || sectionTitle!.isEmpty ? CommonDimen.shared.default_padding : 4.0
            Text(message)
                .padding(.top, topPadding)
                .padding(.bottom, CommonDimen.shared.default_padding)
                .padding(.horizontal, CommonDimen.shared.default_padding)
                .font(.system(size: 15))
        }
        
        .frame(maxWidth: .infinity)
        .background(Color(hex: CommonColors.shared.content_card_background_color))
        .cornerRadius(CommonDimen.shared.default_corner_radius)
        .foregroundColor(.black)
        .shadow(radius: 4)
        .frame(width: UIScreen.main.bounds.width * 0.9)
        .padding(CommonDimen.shared.padding_8)
    }
}

struct ChangeSkinTypeButton: View {
    var action: () -> Void
    
    var body: some View {
        
        Button(CommonStrings_User.shared.button_change_skin_type, action: action)
            .padding()
            .background(Color.black)
            .font(.system(size: 14))
            .foregroundColor(Color.white)
            .cornerRadius(CommonDimen.shared.default_corner_radius)
        
    }
}

enum CardPosition {
    case before, after, above, below
}

struct UvIndexMessageSection<AdditionalView: View>: View {
    var message: String
    var additionalView: (() -> AdditionalView)?
    var position: CardPosition
    var sectionTitle: String?
    
    var body: some View {
        VStack {
            Divider()
                .padding(.horizontal, 16)
            
            VStack(spacing: 8) {
                if let sectionTitle = sectionTitle {
                    Text(sectionTitle)
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundColor(.black)
                        .padding(.top, CommonDimen.shared.padding_8)
                }
                let topPadding = sectionTitle == nil || sectionTitle!.isEmpty ? CommonDimen.shared.default_padding : 4.0
                Text(message)
                    .font(.system(size: 15))
                    .foregroundColor(.black)
                    .padding(.top, topPadding)
                    .padding(.bottom, CommonDimen.shared.default_padding)
                    .frame(maxWidth: .infinity)
                
                if position == .above, let additionalView = additionalView {
                    additionalView()
                        .padding(.top, 8)
                } else if position == .below, let additionalView = additionalView {
                    additionalView()
                        .padding(.bottom, 8)
                }
            }
            .padding(.horizontal, CommonDimen.shared.default_padding)
            .padding(.vertical, CommonDimen.shared.padding_8)
            
            Divider()
                .padding(.horizontal, 16)
        }
    }
}

struct UvIndexMessageCardView<AdditionalView: View>: View {
    var message: String
    var additionalView: (() -> AdditionalView)?
    var margin: CGFloat
    var position: CardPosition
    
    @ViewBuilder
    var body: some View {
        switch position {
        case .before, .after:
            HStack { contentView }
        case .above, .below:
            VStack { contentView }
        }
    }
    
    @ViewBuilder
    private var contentView: some View {
        VStack {
            if position == .above, let additionalView = additionalView {
                Spacer().frame(height: margin)
                additionalView()
            }
            
            Text(message)
                .padding(CommonDimen.shared.default_padding)
                .foregroundColor(.black)
                .font(.system(size: 15))
            
            if position == .below, let additionalView = additionalView {
                additionalView()
                Spacer().frame(height: margin)
            }
        }
        .frame(maxWidth: .infinity)
        .background(Color(hex: CommonColors.shared.content_card_background_color))
        .cornerRadius(CommonDimen.shared.default_corner_radius)
        .foregroundColor(.black)
        .shadow(radius: 4)
        .frame(width: UIScreen.main.bounds.width * 0.9)
        .padding(CommonDimen.shared.padding_8)
    }
}



